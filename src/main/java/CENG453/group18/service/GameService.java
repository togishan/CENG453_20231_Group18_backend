package CENG453.group18.service;

import CENG453.group18.entity.*;
import CENG453.group18.enums.GameType;
import CENG453.group18.repository.GameBoardRepository;
import CENG453.group18.repository.GameRepository;
import CENG453.group18.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.*;
import java.util.function.Function;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameBoardRepository gameBoardRepository;
    @Autowired
    PlayerRepository playerRepository;
    @Autowired
    ScoreService scoreService;
    @Transactional
    public Game createSinglePlayerGame(String username)
    {
        Player player = playerRepository.findPlayerByUsername(username);
        Game game = new Game(player, null, null, null, GameType.SinglePlayer);
        gameBoardRepository.save(game.getGameboard());
        return gameRepository.save(game);
    }

    public List<Game> getAllGames()
    {
        return (List<Game>) gameRepository.findAll();
    }

    @Transactional
    public Boolean deleteGame(int id) {
        try {
            if (!gameRepository.existsGameByGameID(id))
            {
                return false;
            }
            gameRepository.deleteGameByGameID(id);
            return true;  // Deletion was successful
        } catch (Exception e) {
            // Log the exception or handle it as needed
            return false;  // Deletion failed
        }
    }

    private Settlement addSettlement(int gameID, int nodeIndex, int playerNo)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game == null )
        {
            return null;
        }
        Settlement settlement = gameRepository.getGameByGameID(gameID).getGameboard().addSettlement(nodeIndex, playerNo);
        return settlement;
    }

    private Road addRoad(int gameID, int edgeIndex, int playerNo)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game == null)
        {
            return null;
        }
        Road road = gameRepository.getGameByGameID(gameID).getGameboard().addRoad(edgeIndex, playerNo);
        return road;
    }

    private Settlement upgradeSettlement(int gameID, int nodeIndex, int playerNo)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game == null)
        {
            return null;
        }
        Settlement settlement = gameRepository.getGameByGameID(gameID).getGameboard().upgradeSettlement(nodeIndex, playerNo);
        return settlement;
    }

    public Game getGameState(int gameID)
    {
        return gameRepository.getGameByGameID(gameID);
    }
    @Transactional
    public Integer playerMove(int gameID, String moveType, int edgeOrNodeIndex, int playerNo) {
        // Fetch game and check player's turn
        Game game = gameRepository.getGameByGameID(gameID);
        if(game.getTurn() != playerNo) return -1;

        // Define move strategies
        Map<String, Function<Integer, Integer>> moveStrategies = new HashMap<>();
        moveStrategies.put("rollDice", (player) -> rollTheDice(gameID));
        moveStrategies.put("addSettlement", (player) -> performSettlementMove(game, edgeOrNodeIndex, player));
        moveStrategies.put("addRoad", (player) -> performRoadMove(game, edgeOrNodeIndex, player));
        moveStrategies.put("upgradeSettlement", (player) -> performUpgradeMove(game, edgeOrNodeIndex, player));
        moveStrategies.put("endTurn", (player) ->  endTurn(gameID));
        // Execute move if valid, else throw exception
        if (moveStrategies.containsKey(moveType)) return moveStrategies.get(moveType).apply(playerNo);
        else throw new IllegalArgumentException("Invalid move type: " + moveType);
    }

    private Integer performSettlementMove(Game game, int edgeOrNodeIndex, int playerNo) {
        if(!game.hasEnoughResourcesForSettlement(playerNo)) {
            return -2;
        }
        if(null == addSettlement(game.getGameID(), edgeOrNodeIndex, playerNo)) {
            return -3;
        }
        game.consumeResourceCards("settlement", playerNo);
        game.incrementPlayerScore(playerNo, 1);
        return 0;
    }

    private Integer performRoadMove(Game game, int edgeOrNodeIndex, int playerNo) {
        if(!game.hasEnoughResourcesForRoad(playerNo)) {
            return -2;
        }
        if(null == addRoad(game.getGameID(), edgeOrNodeIndex, playerNo)) {
            return -3;
        }
        game.consumeResourceCards("road", playerNo);
        return 0;
    }

    private Integer performUpgradeMove(Game game, int edgeOrNodeIndex, int playerNo) {
        if(!game.hasEnoughResourcesForUpgrade(playerNo)) {
            return -2;
        }
        if(null == upgradeSettlement(game.getGameID(), edgeOrNodeIndex, playerNo)) {
            return -4;
        }
        game.consumeResourceCards("upgrade", playerNo);
        game.incrementPlayerScore(playerNo, 1);
        return 0;
    }

    @Transactional
    public Integer rollTheDice(int gameID)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game.getDiceRolled())
        {
            return -5;
        }
        game.setDiceRolled(true);
        Random rand = new Random();
        int currentDice1 = rand.nextInt(1,7);
        int currentDice2 = rand.nextInt(1,7);
        game.setCurrentDice1(currentDice1);
        game.setCurrentDice2(currentDice2);
        game.distributeAllCards(currentDice1 + currentDice2);
        return 0;
    }

    // set the longest road, check winning condition,
    @Transactional
    public Integer endTurn(int gameID) throws NullPointerException
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(!game.getDiceRolled())
        {
            return -6;
        }
        game.setDiceRolled(false);
        game.setTurn(game.getTurn()%4 + 1);
        game.setLongestRoadInTheGame();
        if (game.getPlayer1Score()>=8)
        {
            System.out.println("Player: " + game.getPlayer1().getUsername() + " wins!");
            return endGame(gameID);
        }
        else if (game.getPlayer2Score()>=8)
        {
            System.out.println("Player: " + game.getPlayer2().getUsername() + " wins!");
            return endGame(gameID);
        }
        else if (game.getPlayer3Score()>=8)
        {
            System.out.println("Player: " + game.getPlayer3().getUsername() + " wins!");
            return endGame(gameID);
        }
        else if (game.getPlayer4Score()>=8)
        {
            System.out.println("Player: " + game.getPlayer4().getUsername() + " wins!");
            return endGame(gameID);
        }
        return 0;
    }

    private Integer endGame(int gameID) {
        Game game = gameRepository.getGameByGameID(gameID);

        scoreService.saveScore(game.getPlayer1().getUsername(), game.getPlayer1Score());
        if (game.getGameType() == GameType.Multiplayer) {
            scoreService.saveScore(game.getPlayer2().getUsername(), game.getPlayer2Score());
            scoreService.saveScore(game.getPlayer3().getUsername(), game.getPlayer3Score());
            scoreService.saveScore(game.getPlayer4().getUsername(), game.getPlayer4Score());

        }
        return -7;
    }

    @Transactional
    public void botBuild(int gameID, int playerNo) {
        Game game = gameRepository.getGameByGameID(gameID);

        // Check if the bot has enough resources to build a road
        if (game.hasEnoughResourcesForRoad(playerNo)) {
            // Find a suitable location to place a new road
            Integer roadLocation = game.getGameboard().findAppropriateRoadPlacement(playerNo);

            // If a suitable location is found, build a road there
            if (roadLocation != null) {
                // Call playerMove to add a road at the found location
                // consumeResources is called by playerMove
                playerMove(gameID, "addRoad", roadLocation, playerNo);
            }
        }

        // Check if the bot has enough resources to build a settlement
        if (game.hasEnoughResourcesForSettlement(playerNo)) {
            // Find a suitable location to place a new settlement
            Integer settlementLocation = game.getGameboard().findAppropriateSettlementPlacement(playerNo);

            // If a suitable location is found, build a settlement there
            if (settlementLocation != null) {
                // Call playerMove to add a settlement at the found location
                // consumeResources is called by playerMove
                playerMove(gameID, "addSettlement", settlementLocation, playerNo);
            }
        }

        // Check if the bot has enough resources to upgrade a settlement to a city
        if (game.hasEnoughResourcesForUpgrade(playerNo)) {
            // Find a settlement that can be upgraded to a city
            Integer upgradeableSettlement = game.getGameboard().findUpgradeableSettlement(playerNo);

            // If an upgradeable settlement is found, upgrade it to a city
            if (upgradeableSettlement != null) {
                // Call playerMove to upgrade the settlement to a city
                // consumeResources is called by playerMove
                playerMove(gameID, "upgradeSettlement", upgradeableSettlement, playerNo);
            }
        }
    }

}
