package CENG453.group18.entity;

import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest
{
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private Player player5;
    @BeforeEach
    void init()
    {
        player1 = new Player(1,"oguzhan", "oguzhan@metu.edu.tr", "123", "", "");
        player2 = new Player(1,"oguzhan", "oguzhan@metu.edu.tr", "123", "", "");
        player3 = new Player(1,"ff", "oguzhan@metu.edu.tr", "123", "", "");
        player4 = new Player(1,"oguzhan", "ff", "123", "", "");
        player5 = new Player(2,"oguzhan", "ff", "123", "", "");
    }

    @Test
    @DisplayName("Test Whether Players Are Equal By Using Their IDs Only")
    void testEqual()
    {
        assertEquals(player1, player2);
        assertEquals(player1, player3);
        assertEquals(player1, player4);
    }
    @Test
    @DisplayName("Test Whether Players Are Not Equal By Using Their IDs Only")
    void testNotEqual() {
        assertNotEquals(player1, player5);
    }



}
