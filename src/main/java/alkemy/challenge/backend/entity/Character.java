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
@Table(name="character")
public class Character {

    @Id
    @SequenceGenerator(
            name = "character_sequence",
            sequenceName = "character_sequence",
            allocationSize = 1
    )
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "character_sequence")
    private Long id;

    private String name;

    private String imageUrl;

    private Integer age;

    private Integer weight;

    @Column(columnDefinition="TEXT")
    private String story;

    @ManyToMany(mappedBy = "characters")
    private List<Movie> movies = new ArrayList<>();

    @PreRemove
    private void removeMovieFromCharacters(){
        for(Movie m : movies){
            m.getCharacters().remove(this);
        }
    }
}
