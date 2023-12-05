package CENG453.group18.controller;

import CENG453.group18.entity.Game;
import CENG453.group18.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired GameService gameService;
    @Operation(summary = "Retrieve all Games", tags = { "game", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "There are no Games", content = {
                    @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    @GetMapping("/allGames")
    public ResponseEntity<List<Game>> getAllGames()
    {
        List<Game> games = gameService.getAllGames();
        if(games.isEmpty())
        {
            return ResponseEntity.status(204).body(games);
        }
        return ResponseEntity.ok(gameService.getAllGames());
    }
    @Operation(summary = "Create a game instance", tags = { "game", "create" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json") }),
            })
    @PostMapping("/create")
    public ResponseEntity<Game> createGame()
    {
        Game game = gameService.createGame();

        return ResponseEntity.ok(game);
    }
    @Operation(summary = "Delete a game instance", tags = { "game", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Not found record with that id or Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGame(int id)
    {
        Boolean deleted = gameService.deleteGame(id);
        if (deleted) {
            return ResponseEntity.ok(true);  // Deletion was successful
        }
        else {
            return ResponseEntity.status(500).body(false);  // Deletion failed
        }
    }


}
