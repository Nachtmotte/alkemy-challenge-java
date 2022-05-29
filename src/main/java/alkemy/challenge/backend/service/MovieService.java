package alkemy.challenge.backend.service;

import alkemy.challenge.backend.entity.Character;
import alkemy.challenge.backend.entity.Genre;
import alkemy.challenge.backend.entity.Movie;
import alkemy.challenge.backend.repository.CharacterRepository;
import alkemy.challenge.backend.repository.GenreRepository;
import alkemy.challenge.backend.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepo;

    private final CharacterRepository characterRepo;

    private final GenreRepository genreRepo;

    public Movie get(Long movieId) {
        return movieRepo.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException(("Movie with id " + movieId + " does not exist")));
    }

    public List<Movie> getAll(String title, Long genreId, String order) {
        Sort sort = Sort.by(Sort.Direction.ASC, "id");
        if(title == null && genreId == null && order == null){
            return movieRepo.findAll(sort);
        }
        if(order != null){
            if(order.equals("ASC")){
                sort = Sort.by(Sort.Direction.ASC, "createAt");
            }
            if(order.equals("DESC")){
                sort = Sort.by(Sort.Direction.DESC, "createAt");
            }
        }
        if(genreId != null){
            return movieRepo.findAllWithAllFilters(title, genreId, sort);
        }
        return movieRepo.findAllWithFilters(title, sort);
    }

    public Movie save(Movie movie, List<Long> charactersId, List<Long> genresId) {
        if (charactersId != null && !charactersId.isEmpty()) {
            List<Character> characters = characterRepo.findAllById(charactersId);
            for (Character c : characters) {
                movie.getCharacters().add(c);
            }
        }
        if (genresId != null && !genresId.isEmpty()) {
            List<Genre> genres = genreRepo.findAllById(genresId);
            for (Genre g : genres) {
                movie.getGenres().add(g);
            }
        }
        return movieRepo.save(movie);
    }

    public void delete(Long movieId) {
        try {
            movieRepo.deleteById(movieId);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Movie with id " + movieId + " does no exist");
        }
    }

    @Transactional
    public Movie update(Long movieId, Movie newMovieData) {
        if (newMovieData.getTitle() == null &&
                newMovieData.getImageUrl() == null &&
                newMovieData.getCreatedAt() == null &&
                newMovieData.getRating() == null) {
            throw new IllegalStateException("You must send a property to update");
        }

        Movie movie = movieRepo.findById(movieId)
                .orElseThrow(() -> new IllegalArgumentException(("Movie with id " + movieId + " does not exist")));

        String newTitle = newMovieData.getTitle();
        if (newTitle != null && newTitle.length() > 0
                && !Objects.equals(newTitle, movie.getTitle())) {
            movie.setTitle(newTitle);
        }

        String newImageUrl = newMovieData.getImageUrl();
        if (newImageUrl != null && newImageUrl.length() > 0
                && !Objects.equals(newImageUrl, movie.getImageUrl())) {
            movie.setImageUrl(newImageUrl);
        }

        LocalDate newCreatedAt = newMovieData.getCreatedAt();
        if (newCreatedAt != null && !Objects.equals(newCreatedAt, movie.getCreatedAt())) {
            movie.setCreatedAt(newCreatedAt);
        }

        Integer newRating = newMovieData.getRating();
        if (newRating != null && !Objects.equals(newRating, movie.getRating())) {
            if(newRating < 1 || newRating > 5){
                throw new IllegalStateException("The Movie rating most be between 1 to 5");
            }
            movie.setRating(newRating);
        }

        return movie;
    }
}
