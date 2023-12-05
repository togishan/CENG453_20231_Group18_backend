package CENG453.group18.service;

import CENG453.group18.entity.Game;
import CENG453.group18.entity.GameBoard;
import CENG453.group18.repository.GameBoardRepository;
import CENG453.group18.repository.GameRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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




}
