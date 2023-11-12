package CENG453.group18.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ScoreTest
{
    private Score score1;
    private Score score2;
    private Score score3;
    private Score score4;
    private Score score5;

    @BeforeEach
    void init()
    {
        score1 = new Score(1, 15, null, null);
        score2 = new Score(1, 15, null, null);
        score3 = new Score(1, 10, null, null);
        score4 = new Score(2, 15, null, null);
    }
    @Test
    @DisplayName("Test Whether Scores Are Equal By Using Their IDs Only")
    void testEqual()
    {
        assertEquals(score1, score2);
        assertEquals(score1, score3);
    }
    @Test
    @DisplayName("Test Whether Scores Are Not Equal By Using Their IDs Only")
    void testNotEqual() {
        assertNotEquals(score1, score4);
    }


}
