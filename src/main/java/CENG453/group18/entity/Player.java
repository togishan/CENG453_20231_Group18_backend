package CENG453.group18.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Setter
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Table(name = "PLAYER")
public class Player {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "playerID")
    private Integer id;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

    /*
        A player can have multiple scores,
        A score can only belong to the single player
     */

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL ,fetch = FetchType.LAZY)
    private List<Score> scores = new ArrayList<>();

    @JsonManagedReference
    public List<Score> getScores()
    {
        return scores;
    }
    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Player player)
        {
            return this.id.equals(player.getId());
        }
        return false;
    }

}
