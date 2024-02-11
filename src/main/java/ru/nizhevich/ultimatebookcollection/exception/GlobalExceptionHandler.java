package ru.nizhevich.ultimatebookcollection.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionModel> handleMissingParams(MissingServletRequestParameterException ex) {
        return new ResponseEntity<>(new ExceptionModel(ex.getMessage(), "В запросе отсутсвует обязательный параметр"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionModel> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(new ExceptionModel(ex.getMessage(), "Неверный параметр в запросе"), HttpStatus.BAD_REQUEST);
    }
}
