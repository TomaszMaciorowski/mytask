package pl.rootm.mytask.resource;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.rootm.mytask.domain.HttpResponse;
import pl.rootm.mytask.domain.Note;
import pl.rootm.mytask.enumeration.Level;
import pl.rootm.mytask.exception.NoteNotFoundException;
import pl.rootm.mytask.service.NoteService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static pl.rootm.mytask.util.DateUtil.dateTimeFormatter;

@RestController
@RequestMapping(path = "/note")
@RequiredArgsConstructor
public class NoteResource {

    private final NoteService noteService;


    @GetMapping("/all")
    public ResponseEntity<HttpResponse<Note>> getNotes() {
        return ResponseEntity.ok().body(noteService.getNotes());
    }


    @PostMapping("/add")
    public ResponseEntity<HttpResponse<Note>> saveNotes(@RequestBody @Valid Note note) {
        return ResponseEntity.created(
                URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/note/all").toUriString())


        ).body(noteService.saveNote(note));
    }

    @GetMapping("/filter")
    public ResponseEntity<HttpResponse<Note>> filterNotes(@RequestParam(value = "level") Level level) {
        return ResponseEntity.ok().body(noteService.filterNotes(level));
    }

    @PutMapping("/update")
    public ResponseEntity<HttpResponse<Note>> updateNotes(@RequestBody @Valid Note note)  throws NoteNotFoundException {
        return ResponseEntity.ok().body(noteService.updateNote(note));
    }

    @DeleteMapping("/delete/{noteId}")
    public ResponseEntity<HttpResponse<Note>> deleteNotes(@PathVariable(value = "noteId") Long id) throws NoteNotFoundException  {
        return ResponseEntity.ok().body(noteService.deleteNote(id));
    }



/*
    @RequestMapping ("/errors")
    public ResponseEntity<HttpResponse<?>> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                .reason("There is no mapping" + request.getMethod() + "req")
                .status(NOT_FOUND)
                .statusCode(NOT_FOUND.value())
                .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                .build(),NOT_FOUND);
    }
*/


    @RequestMapping("/error")
    public ResponseEntity<HttpResponse<?>> handleError(HttpServletRequest request) {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .developerMessage("There is no mapping for a " + request.getMethod() + " request for this path on the server")
                        .status(NOT_FOUND)
                        .statusCode(NOT_FOUND.value())
                        .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(), NOT_FOUND
        );
    }


}
