package alkemy.challenge.backend.dto.movie;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovieGetBasicDto {

    private Long id;

    private String title;

    private String imageUrl;

    private LocalDate createAt;
}
