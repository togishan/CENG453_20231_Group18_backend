package CENG453.group18.service;

import CENG453.group18.DTO.LoginDTO;
import CENG453.group18.DTO.RegisterDTO;
import CENG453.group18.entity.Player;
import CENG453.group18.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public List<Player> getAllPlayers()
    {
        return (List<Player>) playerRepository.findAll();
    }
    public Player getPlayerBySessionKey(String sessionKey)
    {
        return playerRepository.findPlayerBySessionKey(sessionKey);
    }

    public Player registerPlayer(RegisterDTO registerDTO) throws NoSuchAlgorithmException {

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
    public Player loginPlayer(LoginDTO loginDTO) throws NoSuchAlgorithmException
    {
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
}