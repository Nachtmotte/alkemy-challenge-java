package alkemy.challenge.backend.service;

import alkemy.challenge.backend.entity.Character;
import alkemy.challenge.backend.repository.CharacterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CharacterService {

    private final CharacterRepository characterRepo;

    public List<Character> getCharacters(){
        return characterRepo.findAll();
    }

    public Character save(Character character){
        return characterRepo.save(character);
    }
}
