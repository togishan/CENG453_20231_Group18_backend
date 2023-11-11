package CENG453.group18.controller;

import CENG453.group18.DTO.LoginDTO;
import CENG453.group18.DTO.RegisterDTO;
import CENG453.group18.entity.Player;
import CENG453.group18.service.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
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
                    @Content(schema = @Schema()) })})
    @GetMapping("/allPlayers")
    public ResponseEntity<List<Player>> findAllPlayers()
    {
        List<Player> players = playerService.getAllPlayers();
        if(players.isEmpty())
        {
            return ResponseEntity.status(204).body(players);
        }
        return ResponseEntity.ok(playerService.getAllPlayers());
    }
    @Operation(summary = "Retrieve Player Info from the Database using SessionKey", tags = { "players", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            })
    @GetMapping
    public ResponseEntity<Player> getPlayer(@RequestHeader("Session-Key") String sessionKey)
    {
        return ResponseEntity.ok(playerService.getPlayerBySessionKey(sessionKey));
    }



    @Operation(summary = "Create a new Player if there is no such recorded user with the same name or email" , tags = { "players", "post" })
    @PostMapping("/register")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "User with the same username or email address already exists", content = {
                    @Content(schema = @Schema()) })})
    public ResponseEntity<Player> registerPlayer(RegisterDTO registerDTO) throws NoSuchAlgorithmException {
        Player p = playerService.registerPlayer(registerDTO);
        if(p==null)
        {
            return ResponseEntity.status(204).body(p);
        }
        return ResponseEntity.ok(p);
    }
    @Operation(summary = "Login Player if the credentials matched " , tags = { "players", "post" })
    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Player.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "User credentials do not match", content = {
                    @Content(schema = @Schema()) })})
    public ResponseEntity<Player> loginPlayer(LoginDTO loginDTO) throws NoSuchAlgorithmException {
        Player p = playerService.loginPlayer(loginDTO);
        if(p==null)
        {
            return ResponseEntity.status(204).body(p);
        }
        return ResponseEntity.ok(p);
    }
}
