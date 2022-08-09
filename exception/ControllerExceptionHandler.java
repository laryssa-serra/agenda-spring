package br.com.agenda.exception;

import br.com.agenda.model.entities.InvalidFormatException;
import br.com.agenda.model.entities.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> notFoundException(NotFoundException e, HttpServletRequest request){

        return ResponseEntity.status(404).body(new ExceptionDto (404, e.getMessage()));
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ExceptionDto> InvalidFormatException(InvalidFormatException e, HttpServletRequest request){

        return ResponseEntity.status(404).body(new ExceptionDto(400, e.getMessage()));
    }
}
