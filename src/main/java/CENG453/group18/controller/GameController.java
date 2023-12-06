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
    @Operation(summary = "Add settlement to the gameboard", tags = { "game", "addSettlement" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Settlement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "Placement is not appropriate" ),
    })
    @PostMapping("/addSettlement")
    public ResponseEntity<Settlement> addSettlement(int gameID, int nodeIndex, int playerNo)
    {
        try {
            Settlement settlement = gameService.addSettlement(gameID, nodeIndex, playerNo);
            if(settlement != null)
            {
                return ResponseEntity.ok(settlement);
            }
            else
            {
                return ResponseEntity.status(204).body(null);
            }
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @Operation(summary = "Add road to the gameboard", tags = { "game", "addRoad" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Settlement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "Placement is not appropriate" ),
    })
    @PostMapping("/addRoad")
    public ResponseEntity<Road> addRoad(int gameID, int edgeIndex, int playerNo)
    {
        try {
            Road road = gameService.addRoad(gameID, edgeIndex, playerNo);
            if(road != null)
            {
                return ResponseEntity.ok(road);
            }
            else
            {
                return ResponseEntity.status(204).body(null);
            }
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }
    @Operation(summary = "Upgrade a settlement from level 1 to level 2", tags = { "game", "upgradeSettlement" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Settlement.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "204", description = "Upgrade is not appropriate" ),
    })
    @PostMapping("/upgradeSettlement")
    public ResponseEntity<Settlement> upgradeSettlement(int gameID, int nodeIndex, int playerNo)
    {
        try {
            Settlement settlement = gameService.upgradeSettlement(gameID, nodeIndex, playerNo);
            if(settlement != null)
            {
                return ResponseEntity.ok(settlement);
            }
            else
            {
                return ResponseEntity.status(204).body(null);
            }
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }

}
