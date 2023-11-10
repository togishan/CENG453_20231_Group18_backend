package CENG453.group18.DTO;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScoreDTO {
    private String username;
    private int score;
    private LocalDate creationDate;
}
