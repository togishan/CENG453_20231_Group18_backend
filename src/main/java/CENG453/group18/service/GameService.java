package CENG453.group18.service;

import CENG453.group18.entity.Game;
import CENG453.group18.entity.GameBoard;
import CENG453.group18.entity.Road;
import CENG453.group18.entity.Settlement;
import CENG453.group18.enums.GameType;
import CENG453.group18.repository.GameBoardRepository;
import CENG453.group18.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameBoardRepository gameBoardRepository;
    @Transactional
    public Game createGame(int hostID)
    {
        Game game = new Game(hostID, GameType.SinglePlayer);
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

    private Integer endTurn(int gameID) throws NullPointerException
    {
        return gameRepository.getGameByGameID(gameID).endTurn();
    }
    
    @Transactional
    public Integer playerMove(int gameID, String moveType, int edgeOrNodeIndex, int playerNo) {
        // Fetch game and check player's turn
        Game game = gameRepository.getGameByGameID(gameID);
        if(game.getTurn() != playerNo) return -1;

        // Define move strategies
        Map<String, Function<Integer, Integer>> moveStrategies = new HashMap<>();
        moveStrategies.put("addSettlement", (player) -> performSettlementMove(game, edgeOrNodeIndex, player));
        moveStrategies.put("addRoad", (player) -> performRoadMove(game, edgeOrNodeIndex, player));
        moveStrategies.put("upgradeSettlement", (player) -> performUpgradeMove(game, edgeOrNodeIndex, player));
        moveStrategies.put("endTurn", (player) -> { endTurn(gameID); return 0; });

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
        return 0;
    }

    @Transactional
    public Integer rollTheDice(int gameID)
    {
        return gameRepository.getGameByGameID(gameID).rollTheDice();
    }

    /*@Transactional
    public void botPlay(int gameID, int playerNo)
    {
        // bot plays its turn and ends it
        rollTheDice(gameID);
        gameRepository.getGameByGameID(gameID).botPlay(playerNo);
        endTurn(gameID);
    }*/



    @Transactional
    public Boolean setLongestRoad_LongestRoadOwner(int gameID) throws NullPointerException
    {
        gameRepository.getGameByGameID(gameID).setLongestRoadInTheGame();
        return true;
    }


}
