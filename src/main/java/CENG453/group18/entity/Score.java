package CENG453.group18.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.checkerframework.checker.units.qual.C;

import java.time.LocalDate;
import java.util.Date;

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
    private Integer id;
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
            return this.id.equals(score.id);
        }
        return false;
    }
}
