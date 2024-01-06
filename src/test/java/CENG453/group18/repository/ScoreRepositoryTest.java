package CENG453.group18.repository;

//import CENG453.group18.entity.Player;
import CENG453.group18.entity.Player;
import CENG453.group18.entity.Score;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
    @Autowired PlayerRepository playerRepository;
    private final Score score1 = new Score(1, 5, LocalDate.now(), null);
    private final Score score2 = new Score(2, 10, LocalDate.now().minusDays(6), null);
    private final Score score3 = new Score(3, 15, LocalDate.now().minusDays(7), null);

    private String appropriateName = "oguzhannnfnfafgsdsffdh";
    private String appropriateEmail = "oguzhanngnfdgns@metu.edu.tr";
    private final Player player = new Player(1, appropriateName, appropriateEmail, "123", "RANDOM_SESSION_KEY", "RANDOM_RESET_KEY");
    @Test
    @Order(1)
    @Rollback(false)
    void initializeRepository()
    {
        playerRepository.save(player);
        score1.setOwner(player);
        score2.setOwner(player);
        score3.setOwner(player);
        scoreRepository.save(score1);
        scoreRepository.save(score2);
        scoreRepository.save(score3);
    }
    @Test
    @Order(2)
    void testFindScoresByCreationDateAfterOrderByScoreDesc()
    {
        List<Score> scoreBoard = scoreRepository.findScoresByCreationDateAfterOrderByScoreDesc(LocalDate.now().minusDays(7));
        Integer previousScore = 999999;
        for(int i=0; i<scoreBoard.size(); i++)
        {
            assert scoreBoard.get(i).getScore()<=previousScore;
            previousScore = scoreBoard.get(i).getScore();
        }

    }
    @Test
    @Order(2)
    void testFindAllByOrderByScoreDesc()
    {
        List<Score> scoreBoard = scoreRepository.findAllByOrderByScoreDesc();
        Integer previousScore = 999999;
        for(int i=0; i<scoreBoard.size(); i++)
        {
            assert scoreBoard.get(i).getScore()<=previousScore;
            previousScore = scoreBoard.get(i).getScore();
        }
    }
    @Test
    @Order(3)
    @Rollback(false)
    void destruct()
    {
        scoreRepository.deleteAllScoreByOwnerUsername(appropriateName);
        playerRepository.deletePlayerByUsername(appropriateName);
    }

}
