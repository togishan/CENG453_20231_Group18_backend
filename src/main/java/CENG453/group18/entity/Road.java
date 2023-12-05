package CENG453.group18.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Road {
    // ID of the road in the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roadID;

    // Coordinate of the road on the game board
    @Column(name = "edgeIndex", nullable = false)
    private int edgeIndex;
    // Used to determine which player it belongs to
    @Column(name = "playerNo", nullable = false)
    private int playerNo;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameBoard owner;


}
