package alkemy.challenge.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="genre")
public class Genre {

    @Id
    @SequenceGenerator(
            name = "genre_sequence",
            sequenceName = "genre_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "genre_sequence")
    private Long id;

    private String name;

    private String imageUrl;

    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies = new ArrayList<>();

    @PreRemove
    private void removeGenreFromMovies(){
        for(Movie m : movies){
            m.getGenres().remove(this);
        }
    }
}
