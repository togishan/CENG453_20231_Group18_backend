package CENG453.group18.repository;

//import CENG453.group18.entity.Player;
import CENG453.group18.entity.Score;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ScoreRepositoryTest {
    @Autowired
    private ScoreRepository scoreRepository;
    private final Score score1 = new Score(1, 5, LocalDate.now(), null);
    private final Score score2 = new Score(2, 10, LocalDate.now().minusDays(6), null);
    private final Score score3 = new Score(3, 15, LocalDate.now().minusDays(7), null);
    private final Score score4 = new Score(4, 15, LocalDate.now().minusDays(7), null);

    @Test
    @Order(1)
    @Rollback(false)
    void initializeRepository()
    {
        scoreRepository.save(score1);
        scoreRepository.save(score2);
        scoreRepository.save(score3);
    }
    @Test
    @Order(2)
    void testFindScoresByCreationDateAfterOrderByScoreDesc()
    {
        List<Score> scoreBoard = scoreRepository.findScoresByCreationDateAfterOrderByScoreDesc(LocalDate.now().minusDays(7));
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(score2);
        scores.add(score1);
        assertEquals(scoreBoard, scores);
    }
    @Test
    @Order(2)
    void testFindAllByOrderByScoreDesc()
    {
        List<Score> scoreBoard = scoreRepository.findAllByOrderByScoreDesc();
        ArrayList<Score> scores = new ArrayList<>();
        scores.add(score3);
        scores.add(score2);
        scores.add(score1);
        assertEquals(scoreBoard, scores);
    }
    @Test
    @Order(3)
    void testOrderingWithSameScores()
    {
        scoreRepository.save(score4);
        List<Score> scoreBoard = scoreRepository.findAllByOrderByScoreDesc();
        ArrayList<Score> scoreOrdering1 = new ArrayList<>();
        scoreOrdering1.add(score4);
        scoreOrdering1.add(score3);
        scoreOrdering1.add(score2);
        scoreOrdering1.add(score1);

        ArrayList<Score> scoreOrdering2 = new ArrayList<>();
        scoreOrdering2.add(score3);
        scoreOrdering2.add(score4);
        scoreOrdering2.add(score2);
        scoreOrdering2.add(score1);

        // One of them must be true
        boolean b1 = scoreBoard.equals(scoreOrdering1);
        boolean b2 = scoreBoard.equals(scoreOrdering2);
        assertTrue(b1 ^ b2);
    }
}
