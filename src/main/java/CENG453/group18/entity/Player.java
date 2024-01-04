package CENG453.group18.entity;

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.*;

//import java.util.ArrayList;
//import java.util.List;

import java.util.Objects;

@Transactional
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
    private Integer playerID;
    @Column(name = "username", unique = true, nullable = false)
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "sessionKey")
    private String sessionKey;
    @Column(name = "resetKey")
    private String resetKey;
    /*
        A player can have multiple scores,
        A score can only belong to the single player
     */


    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Player player)
        {
            return this.playerID.equals(player.getPlayerID());
        }
        return false;
    }

    @Override
    public int hashCode() {
        // This method was added to remove the warning related to @Data annotation.
        // @Data annotation includes @EqualsAndHashCode, which generates equals() and hashCode() methods automatically.
        // However, since we have manually defined equals() method, we also need to manually define hashCode() method to maintain the contract between equals() and hashCode().
        return Objects.hash(playerID);
    }
}
