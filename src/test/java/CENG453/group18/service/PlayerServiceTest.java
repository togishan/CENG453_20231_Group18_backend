package CENG453.group18.service;

import CENG453.group18.DTO.LoginDTO;
import CENG453.group18.DTO.RegisterDTO;
import CENG453.group18.entity.Player;
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
    @Order(1)
    @Rollback(false)
    void testRegisterNewPlayer() throws NoSuchAlgorithmException {
        RegisterDTO appropriateRegisterDTO1 = new RegisterDTO("oguzhan", "oguzhan@metu.edu.tr","123");
        RegisterDTO appropriateRegisterDTO2 = new RegisterDTO("oguzhan2", "oguzhan2@metu.edu.tr","321");

        Player player1 = playerService.registerPlayer(appropriateRegisterDTO1);
        Player player2 = playerService.registerPlayer(appropriateRegisterDTO2);

        assertNotNull(player1);
        assertNotNull(player2);


    }

    @Test
    @Order(2)
    void testRegisterAlreadyExistingCredentials() throws NoSuchAlgorithmException {
        RegisterDTO notAppropriateRegisterDTO1 = new RegisterDTO("oguzhan", "oguz@metu.edu.tr","123");
        RegisterDTO notAppropriateRegisterDTO2 = new RegisterDTO("oguz", "oguzhan@metu.edu.tr","123");

        Player player1 = playerService.registerPlayer(notAppropriateRegisterDTO1);
        Player player2 = playerService.registerPlayer(notAppropriateRegisterDTO2);

        assertNull(player1);
        assertNull(player2);
    }

    @Test
    @Order(2)
    void testLoginWithCorrectCredentialsAndSetSessionKey() throws NoSuchAlgorithmException {
        LoginDTO correctLoginDTO1 = new LoginDTO("oguzhan","123");
        LoginDTO correctLoginDTO2 = new LoginDTO("oguzhan2","321");

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
        LoginDTO wrongLoginDTO1 = new LoginDTO("oguz","123");
        LoginDTO wrongLoginDTO2 = new LoginDTO("oguz2","321");
        LoginDTO wrongLoginDTO3 = new LoginDTO("oguzhan","1234");
        LoginDTO wrongLoginDTO4 = new LoginDTO("oguzhan2","3210");

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
            if(players.get(i).getEmail().equals("oguzhan@metu.edu.tr"))
            {
                resetKey = players.get(i).getResetKey();
                break;
            }
        }
        assertNull(resetKey);

        boolean bool = playerService.sendEmail("oguzhan@metu.edu.tr");
        assertTrue(bool);

        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getEmail().equals("oguzhan@metu.edu.tr"))
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
    @Order(2)
    void testChangePassword() throws NoSuchAlgorithmException {
        String resetKey = null;
        String previousPassword = null;
        String newPassword = null;
        boolean bool1 = playerService.sendEmail("oguzhan@metu.edu.tr");
        List<Player> players = playerService.getAllPlayers();

        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getEmail().equals("oguzhan@metu.edu.tr"))
            {
                resetKey = players.get(i).getResetKey();
                previousPassword = players.get(i).getPassword();
                break;
            }
        }

        boolean bool2 = playerService.changePassword(resetKey, "456");
        assertTrue(bool2);
        for (int i = 0; i < players.size(); i++) {
            if(players.get(i).getEmail().equals("oguzhan@metu.edu.tr"))
            {
                newPassword = players.get(i).getPassword();
                break;
            }
        }
        assertNotEquals(previousPassword, newPassword);
    }
}
