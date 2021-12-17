package pl.rootm.mytask.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import pl.rootm.mytask.domain.HttpResponse;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;
import static pl.rootm.mytask.util.DateUtil.dateTimeFormatter;

@RestControllerAdvice
@Slf4j
public class HandleException extends ResponseEntityExceptionHandler implements ErrorController {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage());
        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
        String fieldsMessage = fieldErrorList.stream().map(FieldError::getDefaultMessage).collect(Collectors.joining(", "));

        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("Invalid fields:" + fieldsMessage)
                        .developerMessage(ex.getMessage())
                        .status(status)
                        .statusCode(status.value())
                        .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(),status);


    }


    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("Mamy Błąd !!! :" + ex.getMessage() )
                        .developerMessage(ex.getMessage())
                        .status(status)
                        .statusCode(status.value())
                        .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(),status);

    }

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<HttpResponse<?>> illegalStateException(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("IllegalStateException Wyjątek !!");
        return createHttpErrorResponse(BAD_REQUEST,ex.getMessage(),ex);
    }


    @ExceptionHandler(NoteNotFoundException.class)
    protected ResponseEntity<HttpResponse<?>> noteNotFoundException(NoteNotFoundException ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return createHttpErrorResponse(BAD_REQUEST,ex.getMessage(),ex);
    }

    @ExceptionHandler(NoResultException.class)
    protected ResponseEntity<HttpResponse<?>> NoResultException(NoResultException ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return createHttpErrorResponse(BAD_REQUEST,ex.getMessage(),ex);
    }

    @ExceptionHandler(ServletException.class)
    protected ResponseEntity<HttpResponse<?>> servletException(ServletException ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return createHttpErrorResponse(BAD_REQUEST,ex.getMessage(),ex);
    }

    @ExceptionHandler(NoSuchElementException.class)
    protected ResponseEntity<HttpResponse<?>> noSuchElementException(NoSuchElementException ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return createHttpErrorResponse(BAD_REQUEST,ex.getMessage(),ex);
    }



    @ExceptionHandler(Exception.class)
    protected ResponseEntity<HttpResponse<?>> exception(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return createHttpErrorResponse(BAD_REQUEST,ex.getMessage(),ex);
    }

    private ResponseEntity<HttpResponse<?>> createHttpErrorResponse(HttpStatus httpStatus, String reason, Exception ex)
    {
        return new ResponseEntity<>(
                HttpResponse.builder()
                        .reason("Mamy Błąd !!! :" + ex.getMessage() )
                        .developerMessage(ex.getMessage())
                        .status(httpStatus)
                        .statusCode(httpStatus.value())
                        .timeSteamp(LocalDateTime.now().format(dateTimeFormatter()))
                        .build(),httpStatus);

    }
}
