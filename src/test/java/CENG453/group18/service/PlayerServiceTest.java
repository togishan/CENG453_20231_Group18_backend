package CENG453.group18.service;

import CENG453.group18.DTO.LoginDTO;
import CENG453.group18.DTO.RegisterDTO;
import CENG453.group18.entity.Player;
import CENG453.group18.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerServiceTest
{
    @Autowired
    private PlayerService playerService;
    @Test
    @Order(0)
    @Rollback(false)
    void deletePlayersInitially() throws NoSuchAlgorithmException {
        playerService.deletePlayer("oguzhan4533");
        playerService.deletePlayer("oguzhan4534");
    }

    @Test
    @Order(1)
    @Rollback(false)
    void testRegisterNewPlayer() throws NoSuchAlgorithmException {
        RegisterDTO appropriateRegisterDTO1 = new RegisterDTO("oguzhan4533", "oguzhan4533@metu.edu.tr","123");
        RegisterDTO appropriateRegisterDTO2 = new RegisterDTO("oguzhan4534", "oguzhan4534@metu.edu.tr","321");

        Player player1 = playerService.registerPlayer(appropriateRegisterDTO1);
        Player player2 = playerService.registerPlayer(appropriateRegisterDTO2);

        assertNotNull(player1);
        assertNotNull(player2);
    }

    @Test
    @Order(2)
    void testRegisterAlreadyExistingCredentials() throws NoSuchAlgorithmException {
        RegisterDTO notAppropriateRegisterDTO1 = new RegisterDTO("oguzhan4533", "oguz453413@metu.edu.tr","123");
        RegisterDTO notAppropriateRegisterDTO2 = new RegisterDTO("oguz2134324", "oguzhan4533@metu.edu.tr","123");

        Player player1 = playerService.registerPlayer(notAppropriateRegisterDTO1);
        Player player2 = playerService.registerPlayer(notAppropriateRegisterDTO2);

        assertNull(player1);
        assertNull(player2);
    }

    @Test
    @Order(2)
    void testLoginWithCorrectCredentialsAndSetSessionKey() throws NoSuchAlgorithmException {
        LoginDTO correctLoginDTO1 = new LoginDTO("oguzhan4533","123");
        LoginDTO correctLoginDTO2 = new LoginDTO("oguzhan4534","321");

        Player player1 = playerService.loginPlayer(correctLoginDTO1);
        Player player2 = playerService.loginPlayer(correctLoginDTO2);

        assertNotNull(player1);
        assertNotNull(player2);

        assertNotNull(player1.getSessionKey());
        assertNotNull(player2.getSessionKey());
    }

    @Test
    @Order(2)
    void testLoginWithWrongCredentials() throws NoSuchAlgorithmException {
        LoginDTO wrongLoginDTO1 = new LoginDTO("oguz2134324","123");
        LoginDTO wrongLoginDTO2 = new LoginDTO("dasdasac2","321");
        LoginDTO wrongLoginDTO3 = new LoginDTO("oguzhan4533","1234");
        LoginDTO wrongLoginDTO4 = new LoginDTO("oguzhan4534","3210");

        Player player1 = playerService.loginPlayer(wrongLoginDTO1);
        Player player2 = playerService.loginPlayer(wrongLoginDTO2);
        Player player3 = playerService.loginPlayer(wrongLoginDTO3);
        Player player4 = playerService.loginPlayer(wrongLoginDTO4);

        assertNull(player1);
        assertNull(player2);
        assertNull(player3);
        assertNull(player4);
    }

    @Test
    @Order(2)
    void testSetResetKeyBySendingEmailToRegisteredEmailAddress() throws NoSuchAlgorithmException {
        String resetKey = null;
        List<Player> players = playerService.getAllPlayers();

        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getEmail().equals("oguzhan4533@metu.edu.tr"))
            {
                resetKey = players.get(i).getResetKey();
                break;
            }
        }
        assertNull(resetKey);

        boolean bool = playerService.sendEmail("oguzhan4533@metu.edu.tr");
        assertTrue(bool);

        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getEmail().equals("oguzhan4533@metu.edu.tr"))
            {
                resetKey = players.get(i).getResetKey();
                break;
            }
        }
        assertNotNull(resetKey);
    }

    @Test
    @Order(2)
    void testSendingEmailToNotRegisteredEmailAddress() throws NoSuchAlgorithmException {
        boolean bool = playerService.sendEmail("oguz@metu.edu.tr");
        assertFalse(bool);
    }

    @Test
    @Order(3)
    @Rollback(false)
    void destruct() throws NoSuchAlgorithmException {
        playerService.deletePlayer("oguzhan4533");
        playerService.deletePlayer("oguzhan4534");
    }

}
