package alkemy.challenge.backend.dto.character;

import lombok.Data;

@Data
public class CharacterGetBasicDto {

    private Long id;

    private String name;

    private String imageUrl;
}
