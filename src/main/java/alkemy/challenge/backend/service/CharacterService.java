package alkemy.challenge.backend.service;

import alkemy.challenge.backend.entity.Character;
import alkemy.challenge.backend.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepo;

    public Character get(Long characterId) {
        return characterRepo.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("Character with id " + characterId + " does no exist"));
    }

    public List<Character> getAll(String name, Integer age, Integer weight, Long movieId) {
        if(name == null && age == null && weight == null && movieId == null){
            return characterRepo.findAll();
        }
        if(movieId != null){
            return characterRepo.findAllWithFilters(name, age, weight, movieId);
        }
        return characterRepo.findWithFilters(name, age, weight);
    }

    public Character save(Character character) {
        return characterRepo.save(character);
    }

    public void delete(Long characterId) {
        try {
            characterRepo.deleteById(characterId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Character with id " + characterId + " does no exist");
        }
    }

    @Transactional
    public Character update(Long characterId, Character newCharacterData) {
        if (newCharacterData.getName() == null &&
                newCharacterData.getImageUrl() == null &&
                newCharacterData.getAge() == null &&
                newCharacterData.getWeight() == null &&
                newCharacterData.getStory() == null) {
            throw new IllegalStateException("You must send a property to update");
        }

        Character character = characterRepo.findById(characterId)
                .orElseThrow(() -> new IllegalArgumentException("Character with id " + characterId + " does no exist"));

        String newName = newCharacterData.getName();
        if (newName != null && newName.length() > 0
                && !Objects.equals(newName, character.getName())) {
            character.setName(newName);
        }

        String newImageUrl = newCharacterData.getImageUrl();
        if (newImageUrl != null && newImageUrl.length() > 0
                && !Objects.equals(newImageUrl, character.getImageUrl())) {
            character.setImageUrl(newImageUrl);
        }

        Integer newAge = newCharacterData.getAge();
        if (newAge != null && !Objects.equals(newAge, character.getAge())) {
            character.setAge(newAge);
        }

        Integer newWeight = newCharacterData.getWeight();
        if (newWeight != null && !Objects.equals(newWeight, character.getWeight())) {
            character.setWeight(newWeight);
        }

        String newStory = newCharacterData.getStory();
        if (newStory != null && newStory.length() > 0
                && !Objects.equals(newStory, character.getStory())) {
            character.setStory(newStory);
        }

        return character;
    }
}
