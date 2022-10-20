package exception;

import org.hibernate.ObjectNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class ExceptionsHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(ObjectNotFoundException e) {
        return new ApiError(
                HttpStatus.NOT_FOUND,
                List.of(),
                "Object not found",
                e.getMessage(),
                LocalDateTime.now()
                );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleEntityAlreadyExistsException(EntityAlreadyExistsException e) {
        return new ApiError(
                HttpStatus.CONFLICT,
                List.of(),
                "Some fields aren't unique",
                e.getMessage(),
                LocalDateTime.now()
        );
    }
}