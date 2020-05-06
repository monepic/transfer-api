package com.rbs.casestudy.transferapi.rest;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.rbs.casestudy.transferapi.service.ex.ServiceException;

/**
 * Handle backend exceptions, and provide the appropriate response
 * @author ed
 *
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * return 404 error for EntityNotFoundExceptions
     * @param ex
     * @return ResponseEntity
     */
    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<Void> handleEntityNotFound(EntityNotFoundException ex){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        return new ResponseEntity<>(new RequestErrors(ex.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Object serviceException(ServiceException ex) {
        return new RequestErrors(ex);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object everythingElse(Exception ex) {
        log.debug("Handled generic request exception", ex);
        return ex.getMessage();
    }

    /**
     * For returning validation field errors as JSON
     * @author ed
     */
    public static class SubmissionFieldError {

        private String field, message;
        private Object rejectedValue;

        public SubmissionFieldError(FieldError fe) { 
            this.field = fe.getField();
            this.message = fe.getDefaultMessage();
            this.rejectedValue = fe.getRejectedValue();
        }

        public String getField() { return field; }
        public String getMessage() { return message; }
        public Object getRejectedValue() { return rejectedValue; }
    }

    /**
     * For returning validation errors as JSON
     * @author ed
     */
    public static class RequestErrors {

        private final List<SubmissionFieldError> fieldErrors;
        private final List<String> errors;

        public RequestErrors(ServiceException se) {
            this.errors = Collections.singletonList(se.getMessage());
            this.fieldErrors = null;
        }

        public RequestErrors(BindingResult br) {
            this.fieldErrors = br.getFieldErrors().stream()
                    .map(SubmissionFieldError::new)
                    .collect(Collectors.toList());

            this.errors = br.getGlobalErrors().stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.toList());
        }

        @JsonInclude(value = Include.NON_EMPTY)
        public List<SubmissionFieldError> getFieldErrors() { return fieldErrors; }

        @JsonInclude(value = Include.NON_EMPTY)
        public List<String> getErrors() { return errors; }
    }

}