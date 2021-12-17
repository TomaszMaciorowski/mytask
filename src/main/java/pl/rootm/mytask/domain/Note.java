package pl.rootm.mytask.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import pl.rootm.mytask.enumeration.Level;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Data
public class Note implements Serializable {
    @Id @GeneratedValue( strategy = GenerationType.IDENTITY)
    @Column (nullable = false , updatable = false )
    private Long id;
    @NotNull( message = "Tile of this note cannot be null")
    @NotEmpty ( message = "Tile of this note cannot be empty")
    private String title;
    @NotNull( message = "Description of this note cannot be null")
    @NotEmpty ( message = "Description of this note cannot be empty")
    private String description;
    private Level level;
    @JsonFormat ( shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss" , timezone = "America/New_York" )
    private LocalDateTime createAt;
}
