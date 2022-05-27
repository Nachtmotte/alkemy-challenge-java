package alkemy.challenge.backend.dto.character;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CharacterPostDto {

    @NotBlank(message = "The Name of the Character is required")
    private String name;

    @NotBlank(message = "The Image URL of the Character is required")
    private String imageUrl;

    private Integer age;

    private Integer weight;

    @NotBlank(message = "The Story of the Character is required")
    private String story;
}
