package CENG453.group18.entity;

import lombok.*;
import jakarta.persistence.*;

@Getter
@Setter
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Table(name = "Player")
public class Player {
    @Id
    @Column(name = "username")
    private String username;
    @Column(name = "email", unique = true, nullable = false)
    private String email;
    @Column(name = "password", nullable = false)
    private String password;

}
