package alkemy.challenge.backend.controller;

import alkemy.challenge.backend.controller.util.ResponseEntityUtil;
import alkemy.challenge.backend.dto.character.CharacterGetBasicDto;
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

    @GetMapping("/{characterId}")
    public ResponseEntity<Map<String, Object>> getCharacter(@PathVariable("characterId") Long characterId) {

        Character character;
        try {
            character = characterService.get(characterId);
        } catch (IllegalArgumentException e) {
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        }

        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "character", character);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCharacters(
            @RequestParam(value = "name", required = false)String name,
            @RequestParam(value = "age", required = false)Integer age,
            @RequestParam(value = "weight", required = false)Integer weight,
            @RequestParam(value = "movie", required = false)Long movieId) {

        List<Character> characters = characterService.getAll(name, age, weight, movieId);
        List<CharacterGetBasicDto> charactersDto =
                mapper.map(characters, new TypeToken<List<CharacterGetBasicDto>>() {
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

    @DeleteMapping("/{characterId}")
    public ResponseEntity<Map<String, Object>> deleteCharacter(@PathVariable("characterId") Long characterId) {
        try {
            characterService.delete(characterId);
        } catch (IllegalArgumentException e) {
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        }
        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "", null);
    }

    @PatchMapping("/{characterId}")
    public ResponseEntity<Map<String, Object>> updateCharacter(
            @PathVariable("characterId") Long characterId,
            @RequestBody Character newCharacterData) {
        try {
            newCharacterData = characterService.update(characterId, newCharacterData);
        } catch (IllegalArgumentException e) {
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntityUtil.generateResponse(HttpStatus.BAD_REQUEST, "message", e.getMessage());
        }
        CharacterGetDetailedDto characterDto = mapper.map(newCharacterData, CharacterGetDetailedDto.class);
        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "character", characterDto);
    }
}
