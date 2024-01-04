package CENG453.group18.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
//import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;
//import org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;
//import java.util.Date;

import java.util.Objects;

@Transactional
@Getter
@Setter
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Table(name = "SCORE")
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scoreID")
    private Integer scoreID;
    @Column(name = "score", nullable = false)
    private Integer score;
    @Column(name= "creation_date", nullable = false)
    private LocalDate creationDate;
    /*
        A player can have multiple scores,
        A score can only belong to the single player
     */
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player owner;


    @JsonBackReference
    public Player getOwner()
    {
        return owner;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Score score)
        {
            return this.scoreID.equals(score.getScoreID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        // This method was added to remove the warning related to @Data annotation.
        // @Data annotation includes @EqualsAndHashCode, which generates equals() and hashCode() methods automatically.
        // However, since we have manually defined equals() method, we also need to manually define hashCode() method to maintain the contract between equals() and hashCode().
        return Objects.hash(scoreID);
    }
}
