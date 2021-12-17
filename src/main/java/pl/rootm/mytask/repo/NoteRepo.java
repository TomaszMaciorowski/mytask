package pl.rootm.mytask.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rootm.mytask.domain.Note;
import pl.rootm.mytask.enumeration.Level;

import java.util.List;

public interface NoteRepo extends JpaRepository<Note,Long> {
    List<Note> findByLevel(Level level);
    void deleteNotById(Long id);
}
