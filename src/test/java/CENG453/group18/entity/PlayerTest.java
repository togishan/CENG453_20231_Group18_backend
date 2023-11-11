package CENG453.group18.entity;

import org.junit.Before;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest
{
    private Player player1;
    private Player player2;
    private Player player3;
    @BeforeEach
    void initAll()
    {
        player1 = new Player(1,"oguzhan", "oguzhan@metu.edu.tr", "123", "", "", new ArrayList<Score>());
        player2 = new Player(1,"oguzhan", "oguzhan@metu.edu.tr", "123", "", "", new ArrayList<Score>());
        player3 = new Player(2,"oguzhan", "oguzhan@metu.edu.tr", "123", "", "", new ArrayList<Score>());
    }

    @Test
    @DisplayName("Test Whether Players Are Equal By Using Their IDs")
    void testEqual()
    {
        assertEquals(player1, player2);
    }
    @Test
    @DisplayName("Test Whether Players Are Not Equal By Using Their IDs")
    void testNotEqual() {
        assertNotEquals(player1, player3);
        assertNotEquals(player2, player3);
    }



}
