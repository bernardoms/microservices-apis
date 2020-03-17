package com.bernardoms.teamsapi.controller;

import com.bernardoms.teamsapi.exception.TeamAlreadyExistsException;
import com.bernardoms.teamsapi.exception.TeamNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    private ResponseEntity<?> handleConflictException(Exception e, HttpServletRequest request) {
        HashMap<Object, Object> error = mountError(e, "1");
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        HashMap<Object, Object> error = mountError(e, "2");
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TeamNotFoundException.class)
    private ResponseEntity<?> handleCampaignNotFoundException(TeamNotFoundException e) {
        HashMap<Object, Object> error = mountError(e, "3");
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TeamAlreadyExistsException.class)
    private ResponseEntity<?> handleCampaignNotFoundException(TeamAlreadyExistsException e) {
        HashMap<Object, Object> error = mountError(e, "3");
        log.error("Error on processing the  request", e);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    private HashMap<Object, Object> mountError(Exception e, String errorCode) {
        var error = new HashMap<>();
        error.put("error_code", errorCode);
        error.put("description", e.getMessage());
        return error;
    }
}
