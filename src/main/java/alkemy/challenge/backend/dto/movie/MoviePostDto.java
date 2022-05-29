package alkemy.challenge.backend.dto.movie;

import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class MoviePostDto {

    @NotBlank(message = "The Title of the Movie/Serie is required")
    private String title;

    @NotBlank(message = "The Image URL of the Movie is required")
    private String imageUrl;

    @NotBlank(message = "The Creation Date of the Movie is required")
    private String createAt;

    @NotBlank(message = "The Rating Date of the Movie is required")
    @Size(min=1, max = 5, message = "The Movie rating most be between 1 to 5")
    private String rating;

    private List<Long> charactersId;
}
