package alkemy.challenge.backend.repository;

import alkemy.challenge.backend.entity.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
