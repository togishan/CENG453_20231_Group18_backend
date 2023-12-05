package CENG453.group18.entity;

import CENG453.group18.enums.TileType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tile
{
    // ID of the tile in the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int tileID;
    // Coordinate of the tile on the game board
    @Column(name = "tileIndex", nullable = false)
    private int tileIndex;
    @Column(name = "tileType", nullable = false)
    private TileType tileType;
    @Column(name = "tileNumber", nullable = false)
    private int tileNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameBoard owner;
}
