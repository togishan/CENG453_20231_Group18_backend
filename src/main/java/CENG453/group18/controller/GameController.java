package CENG453.group18.controller;

import CENG453.group18.entity.Game;
//import CENG453.group18.entity.Road;
//import CENG453.group18.entity.Settlement;
import CENG453.group18.entity.Player;
import CENG453.group18.enums.CardType;
import CENG453.group18.repository.PlayerRepository;
import CENG453.group18.repository.GameRepository;
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
import org.springframework.http.HttpStatus;

//import java.sql.SQLException;
import java.util.List;
//import java.util.Set;
import java.util.Map;
import java.util.EnumMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/game")
public class GameController {
    @Autowired GameService gameService;
    @Autowired
    private PlayerRepository playerRepository;
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
    public ResponseEntity<?> createSinglePlayerGame(String username)
    {
        if(username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be null or empty.");
        }

        Player player = playerRepository.findPlayerByUsername(username);
        if(player == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player not found.");
        }

        Game existingGame = gameService.findGameByPlayer(player);
        if(existingGame != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("A game already exists for this player.");
        }

        Game game = gameService.createSinglePlayerGame(username);

        return ResponseEntity.ok(game);
    }

    @PostMapping("/createMultiPlayer")
    public ResponseEntity<?> createMultiPlayerGame(String username1, String username2, String username3, String username4) {
        if (username1 == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username1 cannot be null.");
        }

        List<Player> players = new ArrayList<>();
        String[] usernames = {username1, username2, username3, username4};

        for (String username : usernames) {
            if (username == null) {
                continue;  // Skip this iteration if username is null
            }

            Player player = playerRepository.findPlayerByUsername(username);
            if (player == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player not found for username: " + username);
            }
            players.add(player);

            Game existingGame = gameService.findGameByPlayer(player);
            if (existingGame != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("A game already exists for player: " + username);
            }
        }

        Game game = gameService.createMultiPlayerGame(players);

        return ResponseEntity.ok(game);
    }

    @PostMapping("/createTradeOffer")
    public ResponseEntity<?> createTradeOffer(
        int gameId, int playerNo,
        int offeredBrick, int offeredLumber, int offeredOre,
        int offeredGrain, int offeredWool, int requestedBrick,
        int requestedLumber, int requestedOre, int requestedGrain,
        int requestedWool) {

        // Create maps to hold the offered and requested amounts for each card type
        Map<CardType, Integer> offered = new EnumMap<>(CardType.class);
        offered.put(CardType.BRICK, offeredBrick);
        offered.put(CardType.LUMBER, offeredLumber);
        offered.put(CardType.ORE, offeredOre);
        offered.put(CardType.GRAIN, offeredGrain);
        offered.put(CardType.WOOL, offeredWool);

        Map<CardType, Integer> requested = new EnumMap<>(CardType.class);
        requested.put(CardType.BRICK, requestedBrick);
        requested.put(CardType.LUMBER, requestedLumber);
        requested.put(CardType.ORE, requestedOre);
        requested.put(CardType.GRAIN, requestedGrain);
        requested.put(CardType.WOOL, requestedWool);

        // Create the trade offer
        int tradeOfferId = gameService.createTradeOffer(gameId, playerNo, offered, requested);

        return ResponseEntity.ok(tradeOfferId);
    }

    @PostMapping("/acceptTradeOffer")
    public ResponseEntity<?> acceptTradeOffer(int gameId, int playerNo, int tradeOfferId) {
        boolean success = gameService.acceptTradeOffer(gameId, playerNo, tradeOfferId);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteTradeOffer")
    public ResponseEntity<?> deleteTradeOffer(int gameId, int playerNo, int tradeOfferId) {
        boolean success = gameService.deleteTradeOffer(gameId, playerNo, tradeOfferId);

        if (success) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/getGame")
    public ResponseEntity<?> getGame(Integer gameId) {
        if (gameId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("GameId cannot be null.");
        }

        Game game = gameService.getGameState(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found for id: " + gameId);
        }

        return ResponseEntity.ok(game);
    }

    @Autowired
    private GameRepository gameRepository;

    @GetMapping("/getGameEvents")
    public ResponseEntity<?> getGameEvents(Integer gameId) {
        if (gameId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("GameId cannot be null.");
        }

        Game game = gameRepository.getGameByGameID(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Game not found for id: " + gameId);
        }

        List<String> gameEvents = game.getGameboard().getGameEvents();
        return ResponseEntity.ok(gameEvents);
    }

    @PostMapping("/joinExistingGame")
    public ResponseEntity<?> joinExistingGame(String username) {
        if(username == null || username.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username cannot be null or empty.");
        }

        Player player = playerRepository.findPlayerByUsername(username);
        if(player == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Player not found.");
        }

        Game existingGame = gameService.findGameByPlayer(player);
        if(existingGame == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No game available to join.");
        }

        // Return the existing game that the player has already joined
        return ResponseEntity.ok(existingGame);
    }

    @Operation(summary = "Delete a game instance", tags = { "game", "delete" })
    @ApiResponses({
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "400", description = "Bad request, 'id' was not provided"),
            @ApiResponse(responseCode = "500", description = "Not found record with that id or Internal server error")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteGame(@PathVariable Integer id)
    {
        if(id == null) {
            return ResponseEntity.status(400).body(false);
        }
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
            @ApiResponse(responseCode = "404", description = "Requested game not found"),
            @ApiResponse(responseCode = "409", description = "Conflict, failed to delete game"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping("/playerMove")
    public ResponseEntity<Game> playerMove(Integer gameID, String moveType, Integer edgeOrNodeIndex, Integer playerNo)
    {
        if(gameID == null || edgeOrNodeIndex == null || playerNo == null) {
            // Print the values
            System.out.println("gameID: " + gameID);
            System.out.println("edgeOrNodeIndex: " + edgeOrNodeIndex);
            System.out.println("playerNo: " + playerNo);

            // return an error response or throw an exception
            return ResponseEntity.status(400).body(null);
        }
        try {
            if (!gameService.doesGameExist(gameID)) {
                return ResponseEntity.status(404).body(null);
            }
            Integer result = gameService.playerMove(gameID, moveType, edgeOrNodeIndex, playerNo);
            Game game = gameService.getGameState(gameID);
            System.out.println("gameID: " + gameID);
            System.out.println("Result: " + result);
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
            else if(result == -7) {
                return ResponseEntity.status(409).body(game);  // Failed to delete game
            } 
            else
            {
                return ResponseEntity.status(210).body(game);
            }
        } catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @Operation(summary = "Bot does its automated building actions",
            tags = { "game", "botPlayMove" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {
                    @Content(schema = @Schema(implementation = Game.class), mediaType = "application/json")
            }),
   })
    @PostMapping("/botBuild")
    public ResponseEntity<Game> botBuild(int gameID, int playerNo)
    {
        try {
            gameService.botBuild(gameID, playerNo);
            return ResponseEntity.ok(gameService.getGameState(gameID));
        }catch (HttpServerErrorException.InternalServerError e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
