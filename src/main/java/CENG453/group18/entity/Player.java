package CENG453.group18.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

}
