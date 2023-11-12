package CENG453.group18.service;


import CENG453.group18.DTO.RegisterDTO;
import CENG453.group18.DTO.ScoreDTO;
import CENG453.group18.entity.Player;
import CENG453.group18.entity.Score;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScoreServiceTest
{
    @Autowired
    ScoreService scoreService;
    @Autowired
    private PlayerService playerService;

    @Test
    @Order(1)
    @Rollback(false)
    void init() throws NoSuchAlgorithmException {
        RegisterDTO appropriateRegisterDTO1 = new RegisterDTO("oguzhan", "oguzhan@metu.edu.tr","123");
        RegisterDTO appropriateRegisterDTO2 = new RegisterDTO("oguzhan2", "oguzhan2@metu.edu.tr","321");
    }

    @Test
    @Order(2)
    void testSaveScoreWithDateToday()
    {
        Score score1 = scoreService.saveScore("oguzhan", 32);
        Score score2 = scoreService.saveScore("oguzhan", 64);
        Score score3 = scoreService.saveScore("oguzhan2", 128);
        Score score4 = scoreService.saveScore("oguzhan2", 128);

        assertNotNull(score1);
        assertNotNull(score2);
        assertNotNull(score3);
        assertNotNull(score4);
    }

    @Test
    @Order(3)
    @Rollback(false)
    void addScoresWithVariousDates()
    {
        Score score1 = scoreService.saveScoreByAdjustingDate("oguzhan", 16, LocalDate.now());
        Score score2 = scoreService.saveScoreByAdjustingDate("oguzhan", 24, LocalDate.now().minusDays(6));
        Score score3 = scoreService.saveScoreByAdjustingDate("oguzhan", 15, LocalDate.now().minusDays(7));
        Score score4 = scoreService.saveScoreByAdjustingDate("oguzhan2",18, LocalDate.now().minusDays(29));
        Score score5 = scoreService.saveScoreByAdjustingDate("oguzhan2", 13, LocalDate.now().minusDays(30));

        assertNotNull(score1);
        assertNotNull(score2);
        assertNotNull(score3);
        assertNotNull(score4);
        assertNotNull(score5);
    }

    @Test
    @Order(4)
    void testGetLastWeekScores()
    {
        List<ScoreDTO> scoreBoard = scoreService.getLastWeekScores();
        int previousScore = 999999;
        for (int i = 0; i < scoreBoard.size(); i++)
        {
            assertTrue(scoreBoard.get(i).getCreationDate().isAfter(LocalDate.now().minusDays(7)));
            assertTrue(previousScore > scoreBoard.get(i).getScore());
            previousScore = scoreBoard.get(i).getScore();
        }
    }
    @Test
    @Order(4)
    void testGetLastMonthScores()
    {
        List<ScoreDTO> scoreBoard = scoreService.getLastMonthScores();
        int previousScore = 999999;
        for (int i = 0; i < scoreBoard.size(); i++)
        {
            assertTrue(scoreBoard.get(i).getCreationDate().isAfter(LocalDate.now().minusDays(30)));
            assertTrue(previousScore > scoreBoard.get(i).getScore());
            previousScore = scoreBoard.get(i).getScore();
        }
    }
    @Test
    @Order(4)
    void testGetAllTimeScores()
    {
        List<ScoreDTO> scoreBoard = scoreService.getAllTimeScores();
        int previousScore = 999999;
        for (int i = 0; i < scoreBoard.size(); i++)
        {
            assertTrue(previousScore > scoreBoard.get(i).getScore());
            previousScore = scoreBoard.get(i).getScore();
        }
    }

}
