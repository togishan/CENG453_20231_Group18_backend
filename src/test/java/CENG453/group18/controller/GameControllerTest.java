// FILEPATH: /Users/orientusprime/IdeaProjects/CENG453_20231_Group18_backend/src/test/java/CENG453/group18/controller/GameControllerTest.java

package CENG453.group18.controller;

import CENG453.group18.enums.CardType;
import CENG453.group18.service.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class GameControllerTest {

    @InjectMocks
    private GameController gameController;

    @Mock
    private GameService gameService;

    public GameControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void cheatSuccess() {
        // Arrange
        Map<CardType, Integer> requested = new EnumMap<>(CardType.class);
        requested.put(CardType.BRICK, 1);
        requested.put(CardType.LUMBER, 1);
        requested.put(CardType.ORE, 1);
        requested.put(CardType.GRAIN, 1);
        requested.put(CardType.WOOL, 1);
        when(gameService.cheat(1, 1, requested)).thenReturn(true);

        // Act
        ResponseEntity<?> response = gameController.cheat(1, 1, 1, 1, 1, 1, 1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void cheatFailure() {
        // Arrange
        Map<CardType, Integer> requested = new EnumMap<>(CardType.class);
        requested.put(CardType.BRICK, 1);
        requested.put(CardType.LUMBER, 1);
        requested.put(CardType.ORE, 1);
        requested.put(CardType.GRAIN, 1);
        requested.put(CardType.WOOL, 1);
        when(gameService.cheat(1, 1, requested)).thenReturn(false);

        // Act
        ResponseEntity<?> response = gameController.cheat(1, 1, 1, 1, 1, 1, 1);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}