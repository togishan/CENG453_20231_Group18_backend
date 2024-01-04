package CENG453.group18.repository;

import CENG453.group18.entity.Score;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
//import java.util.Date;
import java.util.List;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findAllByOrderByScoreDesc();
    List<Score> findScoresByCreationDateAfterOrderByScoreDesc(LocalDate date);
}
