package CENG453.group18.service;

import CENG453.group18.DTO.LoginDTO;
import CENG453.group18.DTO.RegisterDTO;
import CENG453.group18.entity.Player;
import CENG453.group18.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;


@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    private static final int TOKEN_LENGTH = 32;
    private static final Logger LOGGER = Logger.getLogger(PlayerService.class.getName());

    public List<Player> getAllPlayers() {
        return (List<Player>) playerRepository.findAll();
    }

    public Player getPlayerBySessionKey(String sessionKey) {
        return playerRepository.findPlayerBySessionKey(sessionKey);
    }

    public Player registerPlayer(RegisterDTO registerDTO) throws NoSuchAlgorithmException {
        if (registerDTO.getPassword() == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        if(playerRepository.existsPlayerByEmail(registerDTO.getEmail()) || playerRepository.existsPlayerByUsername(registerDTO.getUsername()))
        {
            return null;
        }
        else
        {
            String encryptedPassword = hashPassword(registerDTO.getPassword());
            Player player = new Player();
            player.setUsername(registerDTO.getUsername());
            player.setEmail(registerDTO.getEmail());
            player.setPassword(encryptedPassword);
            return playerRepository.save(player);
        }
    }

    public Player loginPlayer(LoginDTO loginDTO) throws NoSuchAlgorithmException {
        Player player = playerRepository.findPlayerByUsername(loginDTO.getUsername());
        if(player != null)
        {
            if(!player.getPassword().equals(hashPassword(loginDTO.getPassword())))
            {

                return null;
            }
            player.setSessionKey(generateSessionKey());
            return playerRepository.save(player);
        }
        return null;
    }

    public boolean changePassword(String resetKey, String newPassword) throws NoSuchAlgorithmException {
        // Find the player with the given reset key
        Player player = playerRepository.findPlayerByResetKey(resetKey);
        if (player != null) {
            // Hash the new password
            String encryptedPassword = hashPassword(newPassword);
            // Update the player's password
            player.setPassword(encryptedPassword);
            // Delete the reset key
            player.setResetKey(null);
            playerRepository.save(player);
            return true;
        } else {
            // No player found with the given reset key
            return false;
        }
    }

    private String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {

            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private String generateSessionKey() {
        return UUID.randomUUID().toString();
    }

    public boolean sendEmail(String email) {
        LOGGER.info("sendEmail called with email: " + email);
        String resetKey = generateSessionKey();
        String resetLink = "http://localhost:8080/swagger-ui/index.html#/players/change-password";
        Player player = playerRepository.findPlayerByEmail(email);

        if (player != null) {
            player.setResetKey(resetKey);
            playerRepository.save(player);
            // Configure SMTP properties
            Properties properties = new Properties();
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.port", "587");
            properties.put("mail.smtp.auth", "true");

            // Create a session with the SMTP server
            Session session = Session.getInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("celal8265@gmail.com", "ndxn ciye pxib vlcr");
                }
            });

            try {
                // Create a new email message
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("celal8265@gmail.com"));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
                message.setSubject("Password Reset");
                message.setText("Dear user,\n\nPlease click on the following link to reset your password: "
                        + resetLink + "\n\nYour reset key is: " + resetKey);
                // Send the email
                Transport.send(message);
                LOGGER.info("Email sent to " + email);
                return true;
            } catch (MessagingException e) {
                LOGGER.severe("Error sending email: " + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }
        LOGGER.warning("Player not found for email " + email);
        return false;
    }

}