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
    @Operation(summary = "Create a game instance", tags = { "game", "create" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json") }),
            })
    @PostMapping("/create")
    public ResponseEntity<Game> createGame(int hostID)
    {
        Game game = gameService.createGame(hostID);

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


    @Operation(summary = "Player can do 4 types of operations: Add settlement, add road, upgrade settlement, end the turn", tags = { "game", "playerMove" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Settlement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "It's not player's turn" ),
            @ApiResponse(responseCode = "205", description = "Not enough resources" ),
            @ApiResponse(responseCode = "206", description = "Not appropriate location" ),
            @ApiResponse(responseCode = "207", description = "Can't upgrade the settlement" ),
    })

    @PostMapping("/playerMove")
    public ResponseEntity<Integer> playerMove(int gameID, String moveType, int edgeOrNodeIndex, int playerNo)
    {
        try {
            Integer result = gameService.playerMove(gameID, moveType, edgeOrNodeIndex, playerNo);
            if(result == 0)
            {
                return ResponseEntity.ok(0);
            }
            else if(result == -1)
            {
                return ResponseEntity.status(204).body(null);
            }
            else if(result == -2)
            {
                return ResponseEntity.status(205).body(null);
            }
            else if(result == -3)
            {
                return ResponseEntity.status(206).body(null);
            }
            else
            {
                return ResponseEntity.status(207).body(null);
            }
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    
    @Operation(summary = "Assigns random number ranging from 2 to 12 to the dice", tags = { "game", "rollTheDice" })
    @ApiResponses({
            @ApiResponse(responseCode = "200" ),
            @ApiResponse(responseCode = "204", description = "No record with that id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/rollTheDice")
    public ResponseEntity<Integer> rollTheDice(int gameID)
    {
        try {
            return ResponseEntity.ok(gameService.rollTheDice(gameID));
        }catch (NullPointerException e) {
            return ResponseEntity.status(204).body(null);
        }
        catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Bot plays its turn", tags = { "game", "botPlay" })
    @ApiResponses({
            @ApiResponse(responseCode = "200" ),
            @ApiResponse(responseCode = "204", description = "No record with that id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/botPlay")
    public ResponseEntity<Boolean> botPlay(int gameID, int playerNo)
    {
        try {
            gameService.botPlay(gameID, playerNo);
            return ResponseEntity.ok(true);
        }catch (NullPointerException e) {
            return ResponseEntity.status(204).body(false);
        }
        catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(false);
        }
    }

    @Operation(summary = "Sets the longest road length and longest road owner player no", tags = { "game", "setLongestRoad" })
    @ApiResponses({
            @ApiResponse(responseCode = "200" ),
            @ApiResponse(responseCode = "204", description = "No record with that id"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/setLongestRoad")
    public ResponseEntity<Boolean> setLongestRoad(int gameID)
    {
        try {
            return ResponseEntity.ok(gameService.setLongestRoad_LongestRoadOwner(gameID));
        }catch (NullPointerException e) {
            return ResponseEntity.status(204).body(null);
        }
        catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
