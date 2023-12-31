package CENG453.group18.service;

import CENG453.group18.DTO.ScoreDTO;
import CENG453.group18.entity.Score;
import CENG453.group18.entity.Player;
import CENG453.group18.repository.PlayerRepository;
import CENG453.group18.repository.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ScoreService {
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PlayerRepository playerRepository;

    // For only testing purposes
    public Score saveScoreByAdjustingDate(String username, int playerScore, LocalDate date)
    {
        Player player = playerRepository.findPlayerByUsername(username);
        if(player != null)
        {
            Score score = new Score();
            score.setOwner(player);
            score.setCreationDate(date);
            score.setScore(playerScore);
            return scoreRepository.save(score);
        }
        else
        {
            return null;
        }
    }
    public Score saveScore(String username, int playerScore)
    {
        Player player = playerRepository.findPlayerByUsername(username);
        if(player != null)
        {
            Score score = new Score();
            LocalDate date = LocalDate.now();
            score.setOwner(player);
            score.setCreationDate(date);
            score.setScore(playerScore);
            return scoreRepository.save(score);
        }
        else
        {
            return null;
        }
    }
    public List<ScoreDTO> getLastWeekScores()
    {
        LocalDate date = LocalDate.now();
        List<Score> scores = scoreRepository.findScoresByCreationDateAfterOrderByScoreDesc(date.minusDays(7));

        Map<String, Integer> summedScores = scores.stream()
                .collect(Collectors.groupingBy(
                        score -> score.getOwner().getUsername(),
                        Collectors.summingInt(Score::getScore)
                ));

        return summedScores.entrySet().stream()
                .map(entry -> new ScoreDTO(entry.getKey(), entry.getValue(), null)) // Set creationDate as needed
                .collect(Collectors.toList());
    }


    public List<ScoreDTO> getLastMonthScores()
    {
        LocalDate date = LocalDate.now();
        List<Score> scores = scoreRepository.findScoresByCreationDateAfterOrderByScoreDesc(date.minusDays(30));

        Map<String, Integer> summedScores = scores.stream()
                .collect(Collectors.groupingBy(
                        score -> score.getOwner().getUsername(),
                        Collectors.summingInt(Score::getScore)
                ));

        return summedScores.entrySet().stream()
                .map(entry -> new ScoreDTO(entry.getKey(), entry.getValue(), null)) // Set creationDate as needed
                .collect(Collectors.toList());
    }
    public List<ScoreDTO> getAllTimeScores()
    {
        List<Score> scores = scoreRepository.findAllByOrderByScoreDesc();

        Map<String, Integer> summedScores = scores.stream()
                .collect(Collectors.groupingBy(
                        score -> score.getOwner().getUsername(),
                        Collectors.summingInt(Score::getScore)
                ));

        return summedScores.entrySet().stream()
                .map(entry -> new ScoreDTO(entry.getKey(), entry.getValue(), null)) // Set creationDate as needed
                .collect(Collectors.toList());
    }
}
