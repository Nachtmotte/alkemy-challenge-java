package alkemy.challenge.backend.repository;

import alkemy.challenge.backend.entity.Character;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    @Query("select c from Character c where (:name is null or lower(c.name) like lower(concat('%', :name, '%'))) and " +
            "(:age is null or c.age = :age) and (:weight is null or c.weight = :weight)")
    List<Character> findWithFilters(@Param("name") String name,
                                    @Param("age") Integer age,
                                    @Param("weight") Integer weight, Sort sort);

    @Query("select c from Character c left join c.movies m where (:movieId is null or m.id = :movieId) and " +
            "(:name is null or lower(c.name) like lower(concat('%', :name, '%'))) and (:age is null or c.age = :age) " +
            "and (:weight is null or c.weight = :weight)")
    List<Character> findAllWithFilters(@Param("name") String name,
                                       @Param("age") Integer age,
                                       @Param("weight") Integer weight,
                                       @Param("movieId") Long movieId, Sort sort);
}
