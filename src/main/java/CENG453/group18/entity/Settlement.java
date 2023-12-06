package CENG453.group18.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Settlement {
    // ID of the settlement in the database
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int settlementID;
    // Coordinate of the settlement on the game board
    @Column(name = "nodeIndex", nullable = false)
    private int nodeIndex;
    // Used to determine which player it belongs to
    @Column(name = "playerNo", nullable = false)
    private int playerNo;
    @Column(name = "settlementLevel", nullable = false)
    private int settlementLevel;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "gameBoard_id")
    private GameBoard owner;

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Settlement settlement)
        {
            return this.getNodeIndex() == settlement.getNodeIndex() && this.getPlayerNo() == settlement.getPlayerNo();
        }
        return false;
    }
}
