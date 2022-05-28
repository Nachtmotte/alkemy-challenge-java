package alkemy.challenge.backend.dto.genre;

import alkemy.challenge.backend.dto.movie.MovieGetBasicDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class GenreGetDetailedDto extends GenreGetBasicDto {

    private String imageUrl;

    private List<MovieGetBasicDto> movies;
}
