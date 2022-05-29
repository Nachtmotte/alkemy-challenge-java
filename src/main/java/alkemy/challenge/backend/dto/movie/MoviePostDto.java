package alkemy.challenge.backend.dto.movie;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Data
public class MoviePostDto {

    @NotBlank(message = "The Title of the Movie/Serie is required")
    private String title;

    @NotBlank(message = "The Image URL of the Movie is required")
    private String imageUrl;

    @NotNull(message = "The Creation Date of the Movie is required")
    private LocalDate createdAt;

    @NotNull(message = "The Rating of the Movie is required")
    @Min(value = 1, message = "The Movie rating must be between 1 to 5")
    @Max(value = 5, message = "The Movie rating must be between 1 to 5")
    private Integer rating;

    private List<Long> charactersId;

    private List<Long> genresId;
}
