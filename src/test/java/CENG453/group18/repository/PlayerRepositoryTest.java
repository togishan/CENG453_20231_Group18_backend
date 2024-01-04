package CENG453.group18.repository;

import CENG453.group18.entity.Player;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerRepositoryTest
{
    @Autowired
    private PlayerRepository playerRepository;
    private final Player player = new Player(1, "oguzhan", "oguzhan@metu.edu.tr", "123", "RANDOM_SESSION_KEY", "RANDOM_RESET_KEY");


    @Test
    @Order(1)
    @Rollback(false)
    void initializeRepository()
    {
        playerRepository.save(player);
    }

    @Test
    @Order(2)
    void testFindPlayerByUsername()
    {
        //Player existingPlayer = playerRepository.findPlayerByUsername("oguzhan");
        Player notExistingPlayer = playerRepository.findPlayerByUsername("oguz");
        assertNull(notExistingPlayer);
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
