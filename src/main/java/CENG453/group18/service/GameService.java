package CENG453.group18.service;

import CENG453.group18.entity.Game;
import CENG453.group18.entity.GameBoard;
import CENG453.group18.entity.Road;
import CENG453.group18.entity.Settlement;
import CENG453.group18.repository.GameBoardRepository;
import CENG453.group18.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GameService {
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameBoardRepository gameBoardRepository;
    @Transactional
    public Game createGame()
    {
        Game game = new Game();
        GameBoard gameBoard = new GameBoard();
        gameBoardRepository.save(gameBoard);
        game.setGameboard(gameBoard);
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

    @Transactional
    public Settlement addSettlement(int gameID, int nodeIndex, int playerNo)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game == null)
        {
            return null;
        }
        Settlement settlement = gameRepository.getGameByGameID(gameID).getGameboard().addSettlement(nodeIndex, playerNo);
        return settlement;
    }
    @Transactional
    public Road addRoad(int gameID, int edgeIndex, int playerNo)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game == null)
        {
            return null;
        }
        Road road = gameRepository.getGameByGameID(gameID).getGameboard().addRoad(edgeIndex, playerNo);
        return road;
    }
    @Transactional
    public Settlement upgradeSettlement(int gameID, int nodeIndex, int playerNo)
    {
        Game game = gameRepository.getGameByGameID(gameID);
        if(game == null)
        {
            return null;
        }
        Settlement settlement = gameRepository.getGameByGameID(gameID).getGameboard().upgradeSettlement(nodeIndex, playerNo);
        return settlement;
    }
}
