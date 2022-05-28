package alkemy.challenge.backend.controller;

import alkemy.challenge.backend.controller.util.ResponseEntityUtil;
import alkemy.challenge.backend.dto.genre.GenreGetDetailedDto;
import alkemy.challenge.backend.dto.genre.GenrePostDto;
import alkemy.challenge.backend.entity.Genre;
import alkemy.challenge.backend.service.GenreService;
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
@RequestMapping(path = "/genres", produces = MediaType.APPLICATION_JSON_VALUE)
public class GenreController {

    private final GenreService genreService;

    private final ModelMapper mapper;

    @GetMapping
    public ResponseEntity<Map<String, Object>> getGenres() {

        List<Genre> genres = genreService.getAll();
        List<GenreGetDetailedDto> genresDto =
                mapper.map(genres, new TypeToken<List<GenreGetDetailedDto>>() {
                }.getType());

        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "genres", genresDto);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createGenre(
            @Valid @RequestBody GenrePostDto requestGenre) {

        Genre newGenre = mapper.map(requestGenre, Genre.class);
        newGenre = genreService.save(newGenre);
        GenreGetDetailedDto genreDto = mapper.map(newGenre, GenreGetDetailedDto.class);

        return ResponseEntityUtil.generateResponse(HttpStatus.CREATED, "genre", genreDto);
    }

    @DeleteMapping("/{genreId}")
    public ResponseEntity<Map<String, Object>> deleteGenre(@PathVariable("genreId") Long genreId) {
        try {
            genreService.delete(genreId);
        } catch (IllegalArgumentException e) {
            return ResponseEntityUtil.generateResponse(HttpStatus.NOT_FOUND, "message", e.getMessage());
        }
        return ResponseEntityUtil.generateResponse(HttpStatus.OK, "", null);
    }
}
