package alkemy.challenge.backend.repository;

import alkemy.challenge.backend.entity.Movie;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    @Query("from Movie m where (:title is null or lower(m.title) like lower(concat('%', :title, '%')))")
    List<Movie> findAllWithFilters(@Param("title") String title, Sort sort);

    @Query("from Movie m left join m.genres g where (:genreId is null or g.id = :genreId)" +
            "and (:title is null or lower(m.title) like lower(concat('%', :title, '%')))")
    List<Movie> findAllWithAllFilters(@Param("title") String title, @Param("genreId") Long genreId, Sort sort);
}
