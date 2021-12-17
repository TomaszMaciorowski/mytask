package pl.rootm.mytask.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Collection;

@Data
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HttpResponse<T> implements Serializable {
    protected String timeSteamp;
    protected int statusCode;
    protected HttpStatus status;
    protected String reason;
    protected String message;
    protected String developerMessage;
    protected Collection<? extends  T> notes;


}
