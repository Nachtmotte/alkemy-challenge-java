package alkemy.challenge.backend.dto.movie;

import lombok.Data;

@Data
public class MovieGetBasicDto {

    private Long id;

    private String title;

    private String imageUrl;
}
