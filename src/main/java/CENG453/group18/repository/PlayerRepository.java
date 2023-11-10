package CENG453.group18.repository;

import CENG453.group18.entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long>{
    Player findPlayerByUsername(String username);
    Player findPlayerByEmail(String email);
    boolean existsPlayerByUsername(String username);
    boolean existsPlayerByEmail(String email);
    void deleteByUsername(String username);
}