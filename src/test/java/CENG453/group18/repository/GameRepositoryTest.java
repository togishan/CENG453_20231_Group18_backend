package CENG453.group18.repository;

import CENG453.group18.entity.Game;

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
    private GameRepository gameRepository;
    private final Game game = new Game(null, null, null, null, GameType.SinglePlayer);

    private  Integer gameID;

    @Test
    @Order(1)
    @Rollback(false)
    void initializeRepository()
    {
        gameRepository.save(game);
        gameID = game.getGameID();
    }

    @Test
    @Order(2)
    void testGetGameByID()
    {
        // test by not existing gameID
        Game notExistingGame = gameRepository.getGameByGameID(111111);
        assertNull(notExistingGame);
        // test by existing gameID
        Game existingGame = gameRepository.getGameByGameID(gameID);
        assertNotNull(existingGame);
    }

    @Test
    @Order(2)
    void testFindPlayerByEmail()
    {
        Player existingPlayer = playerRepository.findPlayerByEmail("oguzhan@metu.edu.tr");
        Player notExistingPlayer = playerRepository.findPlayerByEmail("oguz@metu.edu.tr");
        assertEquals(existingPlayer, player);
        assertNull(notExistingPlayer);
    }

    @Test
    @Order(2)
    void testFindPlayerBySessionKey()
    {
        Player existingPlayer = playerRepository.findPlayerBySessionKey("RANDOM_SESSION_KEY");
        Player notExistingPlayer = playerRepository.findPlayerBySessionKey("KEY_SESSION_RANDOM");
        assertEquals(existingPlayer, player);
        assertNull(notExistingPlayer);
    }

    @Test
    @Order(2)
    void testFindPlayerByResetKey()
    {
        Player existingPlayer = playerRepository.findPlayerByResetKey("RANDOM_RESET_KEY");
        Player notExistingPlayer = playerRepository.findPlayerByResetKey("KEY_RESET_RANDOM");
        assertEquals(existingPlayer, player);
        assertNull(notExistingPlayer);
    }
    @Test
    @Order(2)
    void testExistsPlayerByUsername()
    {
        boolean trueAssertion = playerRepository.existsPlayerByUsername("oguzhan");
        boolean falseAssertion = playerRepository.existsPlayerByUsername("oguz");
        assertTrue(trueAssertion);
        assertFalse(falseAssertion);
    }
    @Test
    @Order(2)
    void testExistsPlayerByEmail()
    {
        boolean trueAssertion = playerRepository.existsPlayerByEmail("oguzhan@metu.edu.tr");
        boolean falseAssertion = playerRepository.existsPlayerByEmail("oguz@metu.edu.tr");
        assertTrue(trueAssertion);
        assertFalse(falseAssertion);
    }
}
