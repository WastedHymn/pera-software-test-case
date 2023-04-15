package com.kadiryuksel.peratestcase.advice;

import com.kadiryuksel.peratestcase.exception.*;
import com.kadiryuksel.peratestcase.response.AlreadyExistsExceptionResponse;
import com.kadiryuksel.peratestcase.response.BadRequestResponse;
import com.kadiryuksel.peratestcase.response.NotFoundExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BadRequestResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        //Source -> https://stackoverflow.com/questions/33663801/how-do-i-customize-default-error-message-from-spring-valid-validation
        String errorMessage = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList())
                .toString()
                .replaceAll("\\[*]*", "");
        BadRequestResponse response = new BadRequestResponse(errorMessage);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    public ResponseEntity<AlreadyExistsExceptionResponse> handleTeamAlreadyExistsException(TeamAlreadyExistsException exception){
        AlreadyExistsExceptionResponse response = new AlreadyExistsExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<BadRequestResponse> handleConstraintViolationException(ConstraintViolationException exception){
        String message = exception.getMessage().split(": ")[1];
        BadRequestResponse response = new BadRequestResponse(message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PlayerAlreadyExistsException.class)
    public ResponseEntity<AlreadyExistsExceptionResponse> handlePlayerAlreadyExistsException(PlayerAlreadyExistsException exception){
        AlreadyExistsExceptionResponse response = new AlreadyExistsExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<NotFoundExceptionResponse> handleTeamNotFoundException(TeamNotFoundException exception){
        NotFoundExceptionResponse response = new NotFoundExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /*
    @ExceptionHandler(GoalkeeperLimitException.class)
    public ResponseEntity<AlreadyExistsExceptionResponse> handleGoalkeeperLimitException(GoalkeeperLimitException exception){
        AlreadyExistsExceptionResponse response = new AlreadyExistsExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
    */

    @ExceptionHandler(PlayerLimitException.class)
    public ResponseEntity<AlreadyExistsExceptionResponse> handlePlayerLimitException(PlayerLimitException exception){
        AlreadyExistsExceptionResponse response = new AlreadyExistsExceptionResponse(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
