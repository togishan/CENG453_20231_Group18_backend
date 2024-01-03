package CENG453.group18.repository;

import CENG453.group18.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;

import CENG453.group18.entity.Player;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game getGameByGameID(int id);
    void deleteGameByGameID(int id);
    boolean existsGameByGameID(int id);
    Game findByPlayer1OrPlayer2OrPlayer3OrPlayer4(Player player1, Player player2, Player player3, Player player4);
}
