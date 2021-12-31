package pl.rootm.mytask.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.rootm.mytask.domain.HttpResponse;
import pl.rootm.mytask.domain.Note;
import pl.rootm.mytask.enumeration.Level;
import pl.rootm.mytask.exception.NoteNotFoundException;
import pl.rootm.mytask.repo.NoteRepo;
import pl.rootm.mytask.util.DateUtil;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static pl.rootm.mytask.util.DateUtil.dateTimeFormatter;


@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class NoteService {

    private final NoteRepo noteRepo;


    // method to get all notes
   public HttpResponse<Note> getNotes() {
       log.info("Pobranie wszystkich not");
       return HttpResponse.<Note>builder()
               .notes(noteRepo.findAll().stream().sorted(Comparator.comparing(Note::getCreateAt).reversed()).collect(Collectors.toList()))
               .message(noteRepo.count() > 0 ? noteRepo.count() + " notes retrived" : "No notes to dispaly")
               .status(OK)
               .statusCode(OK.value())
               .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
               .build();
   }


   // method to filer notes
   public HttpResponse<Note> filterNotes(Level level) {

       List<Note> notes = noteRepo.findByLevel(level);
       log.info("Pobranie wszystkich not  by level", level);
       return HttpResponse.<Note>builder()
               .notes(notes.stream().sorted(Comparator.comparing(Note::getCreateAt).reversed()).collect(Collectors.toList()))
               .message(notes.size() + "notes are of" + level + "importance")
               .status(OK)
               .statusCode(OK.value())
               .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
               .build();
   }


   // method to save to new note

    public HttpResponse<Note> saveNote(Note note) {
        log.info("Saving new note to the database");
        note.setCreateAt(LocalDateTime.now());
        Note newNote = noteRepo.save(note);

        return HttpResponse.<Note>builder()
                .notes(Collections.singleton(newNote))
                .message("Note created succesfully")
                .status(CREATED)
                .statusCode(CREATED.value())
                .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }
   //method to update a note
   public HttpResponse<Note> updateNote(Note note) throws NoteNotFoundException {
       log.info("Update note to the database");
       note.setCreateAt(LocalDateTime.now());
       Optional<Note> optionalNote = Optional.ofNullable(noteRepo.findById(note.getId()))
               .orElseThrow(() -> new NoteNotFoundException("The note not found on the server"));
       Note updateNote = optionalNote.get();
       updateNote.setId(note.getId());
       updateNote.setTitle(note.getTitle());
       updateNote.setDescription(note.getDescription());
       updateNote.setLevel(note.getLevel());
        noteRepo.save(updateNote);

       return HttpResponse.<Note>builder()
               .notes(Collections.singleton(updateNote))
               .message("Note update  succesfully")
               .status(OK)
               .statusCode(OK.value())
               .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
               .build();
   }

   // method to delete a note


    public HttpResponse<Note> deleteNote(Long id) throws NoteNotFoundException {
        log.info("Deletee note to the database  ",id);

        Optional<Note> optionalNote = Optional.ofNullable(noteRepo.findById(id))
                .orElseThrow(() -> new NoteNotFoundException("The note not found on the server"));

        optionalNote.ifPresent(noteRepo::delete);


        return HttpResponse.<Note>builder()
                .notes(Collections.singleton(optionalNote.get()))
                .message("Note deleted succesfully")
                .status(OK)
                .statusCode(OK.value())
                .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build();
    }


}
