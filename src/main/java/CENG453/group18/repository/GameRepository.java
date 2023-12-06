package CENG453.group18.repository;

import CENG453.group18.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game getGameByGameID(int id);
    void deleteGameByGameID(int id);
    boolean existsGameByGameID(int id);
}
