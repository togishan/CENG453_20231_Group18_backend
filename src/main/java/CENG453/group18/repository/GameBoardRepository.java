package CENG453.group18.repository;

import CENG453.group18.entity.GameBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameBoardRepository extends JpaRepository<GameBoard, Integer> {
    public GameBoard getGameBoardById(Integer id);
}