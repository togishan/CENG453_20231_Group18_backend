package CENG453.group18.controller;

import CENG453.group18.entity.Player;
import CENG453.group18.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {
    @Autowired private PlayerService playerService;

    @Operation(summary = "Retrieve all Players", tags = { "players", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There are no Players", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    @GetMapping
    public List<Player> findAllPlayers()
    {
        return playerService.getAllPlayers();
    }

    @Operation(summary = "Create a new Player if there is no such recorded user with the same name or email" , tags = { "players", "post" })
    @PostMapping
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) }) })
    public Player registerPlayer(Player player)
    {
        return playerService.registerPlayer(player);
    }

}
