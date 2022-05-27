package alkemy.challenge.backend.controller;

import alkemy.challenge.backend.controller.util.ResponseEntityUtil;
import alkemy.challenge.backend.dto.character.CharacterGetDetailedDto;
import alkemy.challenge.backend.dto.character.CharacterPostDto;
import alkemy.challenge.backend.entity.Character;
import alkemy.challenge.backend.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/characters", produces = MediaType.APPLICATION_JSON_VALUE)
public class CharacterController {

    private final CharacterService characterService;

    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCharacters() {

        List<Character> characters = characterService.getCharacters();
        List<CharacterGetDetailedDto> charactersDto =
                mapper.map(characters, new TypeToken<List<CharacterGetDetailedDto>>() {
                }.getType());

        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "characters", charactersDto);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createCharacter(
            @Valid @RequestBody CharacterPostDto requestCharacter) {

        Character newCharacter = mapper.map(requestCharacter, Character.class);
        newCharacter = characterService.save(newCharacter);
        CharacterGetDetailedDto characterDto = mapper.map(newCharacter, CharacterGetDetailedDto.class);

        return ResponseEntityUtil.generateResponse(HttpStatus.CREATED, "character", characterDto);
    }
}
