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

    private String appropriateName = "oguzhannnfnfafgsdsffdh";
    private String appropriateEmail = "oguzhanngnfdgns@metu.edu.tr";
    private final Player player = new Player(1, appropriateName, appropriateEmail, "123", "RANDOM_SESSION_KEY", "RANDOM_RESET_KEY");


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
        Player existingPlayer = playerRepository.findPlayerByUsername(appropriateName);
        Player notExistingPlayer = playerRepository.findPlayerByUsername("ongffssbsbsguz");
        assertEquals(existingPlayer.getUsername(), player.getUsername());
        assertNull(notExistingPlayer);
    }

    @Test
    @Order(2)
    void testFindPlayerByEmail()
    {
        Player existingPlayer = playerRepository.findPlayerByEmail(appropriateEmail);
        Player notExistingPlayer = playerRepository.findPlayerByEmail("ogdhggdfngfduz@metu.edu.tr");
        assertEquals(existingPlayer.getUsername(), player.getUsername());
        assertNull(notExistingPlayer);
    }

    @Test
    @Order(2)
    void testFindPlayerBySessionKey()
    {
        Player existingPlayer = playerRepository.findPlayerBySessionKey("RANDOM_SESSION_KEY");
        Player notExistingPlayer = playerRepository.findPlayerBySessionKey("KEY_SESSION_RANDOM");
        assertEquals(existingPlayer.getUsername(), player.getUsername());
        assertNull(notExistingPlayer);
    }

    @Test
    @Order(2)
    void testFindPlayerByResetKey()
    {
        Player existingPlayer = playerRepository.findPlayerByResetKey("RANDOM_RESET_KEY");
        Player notExistingPlayer = playerRepository.findPlayerByResetKey("KEY_RESET_RANDOM");
        assertEquals(existingPlayer.getUsername(), player.getUsername());
        assertNull(notExistingPlayer);
    }
    @Test
    @Order(2)
    void testExistsPlayerByUsername()
    {
        boolean trueAssertion = playerRepository.existsPlayerByUsername(appropriateName);
        boolean falseAssertion = playerRepository.existsPlayerByUsername("mhgifdnbsbfsbds");
        assertTrue(trueAssertion);
        assertFalse(falseAssertion);
    }
    @Test
    @Order(2)
    void testExistsPlayerByEmail()
    {
        boolean trueAssertion = playerRepository.existsPlayerByEmail(appropriateEmail);
        boolean falseAssertion = playerRepository.existsPlayerByEmail("ascsavdavavascasc@metu.edu.tr");
        assertTrue(trueAssertion);
        assertFalse(falseAssertion);
    }
    @Test
    @Order(3)
    @Rollback(false)
    void destruct()
    {
        playerRepository.deletePlayerByUsername(appropriateName);
    }
}
