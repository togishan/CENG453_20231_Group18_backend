package CENG453.group18.service;

import CENG453.group18.entity.Player;
import CENG453.group18.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;

    public Player registerPlayer(Player player)
    {

        if(playerRepository.existsPlayerByEmail(player.getEmail()) || playerRepository.existsPlayerByUsername(player.getUsername()))
        {
            return null;
        }
        else
        {
            return playerRepository.save(player);
        }
    }
    public List<Player> getAllPlayers()
    {
        return (List<Player>) playerRepository.findAll();
    }
    public void deletePlayer(String username)
    {
        if(playerRepository.existsPlayerByUsername(username))
        {
            return;
        }
        else
        {
            playerRepository.delete(playerRepository.findPlayerByUsername(username));
        }
    }

}
