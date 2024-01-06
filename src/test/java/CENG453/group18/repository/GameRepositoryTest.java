package CENG453.group18.repository;

import CENG453.group18.entity.Game;

import CENG453.group18.entity.Player;
import CENG453.group18.enums.GameType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Transactional
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameRepository gameRepository;
    private Game game;
    private Integer gameID;
    private String appropriateName1 = "oguzhancsacsasc22332";
    private String appropriateEmail1 = "oguzhanvdsdvsa22332@metu.edu.tr";

    private Player player;
    @Test
    @Order(1)
    @Rollback(false)
    void deleteUsers()
    {
        playerRepository.deletePlayerByUsername(appropriateName1);
    }
    @Test
    @Order(1)
    @Rollback(false)
    void registerGameHost()
    {
        Player player = new Player();
        player.setUsername(appropriateName1);
        player.setEmail(appropriateEmail1);
        player.setPassword("123");
        playerRepository.save(player);

    }
    @Test
    @Order(2)
    @Rollback(false)
    void initializeRepository()
    {
        game = new Game(player, null, null, null, GameType.SinglePlayer);
        gameID = gameRepository.save(game).getGameID();
        System.out.println(gameID);
    }

    @Test
    @Order(3)
    void testGetGameByID()
    {
        // test by not existing gameID
        Game notExistingGame = gameRepository.getGameByGameID(111111);
        assertNull(notExistingGame);
        System.out.println(gameID);
        // test by existing gameID
        Game existingGame = gameRepository.getGameByGameID(gameID);
        assertNotNull(existingGame);
    }
    @Test
    @Order(4)
    void testDeleteAndExistGameByGameID()
    {
        // check game exists then delete the game and check again
        boolean existingGame = gameRepository.existsGameByGameID(gameID);
        assertTrue(existingGame);
        gameRepository.deleteGameByGameID(gameID);
        boolean notExistingGame = gameRepository.existsGameByGameID(gameID);
        assertFalse(notExistingGame);
    }
    @Test
    @Order(6)
    @Rollback(false)
    void destruct()
    {
        gameRepository.deleteGameByGameID(gameID);
        playerRepository.deletePlayerByUsername(appropriateName1);
    }
}
