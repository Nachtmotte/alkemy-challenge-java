package alkemy.challenge.backend.dto.character;

import alkemy.challenge.backend.dto.movie.MovieGetBasicDto;
import lombok.*;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CharacterGetDetailedDto extends CharacterGetBasicDto {

    private Integer age;

    private Integer weight;

    private String story;

    private List<MovieGetBasicDto> movies;
}
