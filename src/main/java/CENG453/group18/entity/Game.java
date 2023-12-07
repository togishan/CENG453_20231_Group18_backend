package CENG453.group18.entity;


import CENG453.group18.enums.GameType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Game {

    // store it in the frontend and use it when sending api requests
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer gameID;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private GameBoard gameboard;

    // These containers are not the correct implementation make it be stored in the database,
    // The idea is simple: the game will hold all of its players reference
    // and once the game is finished the local scores will be added to player's total
    // score to be displayed in th scoreboard

    private int[] playerIDs;
    private int[] scores;

    // whose turn is now. if the player-1 is playing, then turn is 1 ...
    // if the player-4 is playing, then turn is 4, and again it's player-1's turn and turn is 1
    // don't forget to make a single player authorized to change game until it's turn finishes
    @Column(name = "turn")
    private Integer turn;

    // SinglePlayer or Multiplayer
    @Column(name = "game_type")
    private GameType gameType;

    @Column(name = "current_dice")
    private Integer currentDice;

    // holds a single card deck for each player and each card deck holds multiple cards belonging to player
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerCardDeck> playerCardDeckList = new ArrayList<>();


    // Constructor must be changed by adding and attaching players and additional features
    public Game() {
        this.gameboard = new GameBoard();
        turn = 1;

        // add cards to each player at beginning (resources adjacent to initial settlements)
        // implement distributeCards and add here
        // todo

    }


    // Instead of assigning random value here it can be changed this way:
    // Roll 2 rice, get sum of them and assign the value here
    // In current implementation: roll the dice, save value, assign a pair of values
    // total of which is equal to saved value.
    public Integer rollTheDice()
    {
        Random rand = new Random();
        currentDice = rand.nextInt(2,13);
        return currentDice;
    }

    // bot with playerNo plays its turn
    // human player does not need this automated function. If it is human player's turn
    // then depending on player's choice call api functions separately until endTurn is invoked by the
    // player
    public void botPlay(int playerNo)
    {

        // don't call endTurn or rollTheDice here, they are already called in the botPlay service
        // only implement bot logic here using resource cards, game board and the dice
        // can add sleep function to make it look like a human
        System.out.println(currentDice);
        //todo

    }

    // end the turn and notify the players whose turn is started
    public Integer endTurn()
    {
        turn = turn%4 + 1;
        return turn;
        //todo
    }

    // once the dice is rolled add cards to card decks of players, depend on their settlement placements
    private void distributeCards()
    {
        // todo
    }
}
