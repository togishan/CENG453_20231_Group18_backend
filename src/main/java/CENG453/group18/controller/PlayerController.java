package CENG453.group18.controller;

import CENG453.group18.entity.Player;
import CENG453.group18.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/v1/players")
public class PlayerController {
    @Autowired private PlayerService playerService;

    @GetMapping
    public List<Player> findAllPlayers(){
        return playerService.getAllPlayers();
    }
    @PostMapping
    public Player registerPlayer(Player player)
    {
        return playerService.registerPlayer(player);
    }
}
