package alkemy.challenge.backend.dto.genre;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class GenrePostDto {

    @NotBlank
    private String name;

    @NotBlank
    private String imageUrl;

}
