package alkemy.challenge.backend.service;

import alkemy.challenge.backend.entity.Genre;
import alkemy.challenge.backend.repository.GenreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepo;

    public List<Genre> getAll(){
        return genreRepo.findAll();
    }

    public Genre save(Genre genre){
        return genreRepo.save(genre);
    }

    public void delete(Long genreId){
        try {
            genreRepo.deleteById(genreId);
        }catch (EmptyResultDataAccessException e){
            throw new IllegalArgumentException("Genre with id " + genreId + " does no exist");
        }
    }
}
