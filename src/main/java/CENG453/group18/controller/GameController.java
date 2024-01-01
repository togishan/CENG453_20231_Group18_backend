package CENG453.group18.controller;

import CENG453.group18.entity.Game;
import CENG453.group18.entity.Road;
import CENG453.group18.entity.Settlement;
import CENG453.group18.service.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

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
    @Operation(summary = "Create a single player game instance", tags = { "game", "create" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json") }),
            })
    @PostMapping("/createSinglePlayer")
    public ResponseEntity<Game> createSinglePlayerGame(String username)
    {
        System.out.println(username);
        Game game = gameService.createSinglePlayerGame(username);

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

    @Operation(summary = "Player can perform various game moves: rollDice, addSettlement, addRoad, upgradeSettlement, endTurn",
            tags = { "game", "playerMove" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "204", description = "It's not the player's turn", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "205", description = "Not enough resources", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "206", description = "Not an appropriate location", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "207", description = "Can't upgrade the settlement", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "208", description = "Can't roll the dice more than once for a turn", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "209", description = "Can't end turn without rolling a dice", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "210", description = "Game over", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/playerMove")
    public ResponseEntity<Game> playerMove(int gameID, String moveType, int edgeOrNodeIndex, int playerNo)
    {
        try {
            Integer result = gameService.playerMove(gameID, moveType, edgeOrNodeIndex, playerNo);
            Game game = gameService.getGameState(gameID);
            if(result == 0)
            {
                return ResponseEntity.ok(game);
            }
            else if(result == -1)
            {
                return ResponseEntity.status(204).body(game);
            }
            else if(result == -2)
            {
                return ResponseEntity.status(205).body(game);
            }
            else if(result == -3)
            {
                return ResponseEntity.status(206).body(game);
            }
            else if(result == -4)
            {
                return ResponseEntity.status(207).body(game);
            }
            else if(result == -5)
            {
                return ResponseEntity.status(208).body(game);
            }
            else if(result == -6)
            {
                return ResponseEntity.status(209).body(game);
            }
            else
            {
                return ResponseEntity.status(209).body(game);
            }
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Bot does its automated actions",
            tags = { "game", "botPlayMove" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
   })
    @PostMapping("/botPlay")
    public ResponseEntity<Game> botPlay(int gameID, int playerNo)
    {
        try {
            gameService.botPlay(gameID, playerNo);
            return ResponseEntity.ok(gameService.getGameState(gameID));
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
