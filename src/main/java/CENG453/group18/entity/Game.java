package CENG453.group18.entity;


import CENG453.group18.enums.GameType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameID;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private GameBoard gameboard;

    private int[] playerIDs;
    private int[] scores;
    private int turn;
    private GameType gameType;

    public Game() {
        this.gameboard = new GameBoard();
    }
}
