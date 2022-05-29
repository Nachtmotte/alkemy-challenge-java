package alkemy.challenge.backend.controller;

import alkemy.challenge.backend.controller.util.ResponseEntityUtil;
import alkemy.challenge.backend.dto.movie.MovieGetBasicDto;
import alkemy.challenge.backend.dto.movie.MovieGetDetailedDto;
import alkemy.challenge.backend.dto.movie.MoviePostDto;
import alkemy.challenge.backend.entity.Movie;
import alkemy.challenge.backend.service.MovieService;
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
@RequestMapping(path = "/movies", produces = MediaType.APPLICATION_JSON_VALUE)
public class MovieController {

    private final MovieService movieService;

    private final ModelMapper mapper;

    @GetMapping("/{movieId}")
    public ResponseEntity<Map<String, Object>> getMovie(@PathVariable("movieId") Long movieId) {

        Movie movie;
        try {
            movie = movieService.get(movieId);
        } catch (IllegalArgumentException e) {
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        }

        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "movie", movie);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getMovies(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "genreId", required = false) Long genreId,
            @RequestParam(value = "order", required = false) String order) {

        List<Movie> movies = movieService.getAll(title, genreId, order);
        List<MovieGetBasicDto> moviesDto =
                mapper.map(movies, new TypeToken<List<MovieGetBasicDto>>() {
                }.getType());

        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "movies", moviesDto);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createMovie(
            @Valid @RequestBody MoviePostDto requestMovie){

        Movie newMovie = mapper.map(requestMovie, Movie.class);
        newMovie = movieService.save(newMovie, requestMovie.getCharactersId());
        MovieGetDetailedDto movieDto = mapper.map(newMovie, MovieGetDetailedDto.class);

        return ResponseEntityUtil.generateResponse(HttpStatus.CREATED, "movie", movieDto);
    }

    @DeleteMapping("/{movieId}")
    public ResponseEntity<Map<String, Object>> deleteMovie(@PathVariable("movieId") Long movieId){
        try{
            movieService.delete(movieId);
        }catch (IllegalArgumentException e){
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        }
        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "", null);
    }

    @PatchMapping("/{movieId}")
    public ResponseEntity<Map<String, Object>> updateMovie(
            @PathVariable("movieId") Long movieId,
            @RequestBody Movie newMovieData){
        try{
            newMovieData = movieService.update(movieId, newMovieData);
        }catch (IllegalArgumentException e){
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        }catch (IllegalStateException e){
            return ResponseEntityUtil.generateResponse(HttpStatus.BAD_REQUEST, "message", e.getMessage());
        }
        MovieGetDetailedDto movieDto = mapper.map(newMovieData, MovieGetDetailedDto.class);
        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "movie", movieDto);
    }
}
