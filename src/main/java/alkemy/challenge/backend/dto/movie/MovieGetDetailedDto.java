package alkemy.challenge.backend.dto.movie;

import alkemy.challenge.backend.dto.character.CharacterGetBasicDto;
import alkemy.challenge.backend.dto.genre.GenreGetBasicDto;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MovieGetDetailedDto extends MovieGetBasicDto{

    private Integer rating;

    private List<CharacterGetBasicDto> characters;

    private List<GenreGetBasicDto> genres;
}
