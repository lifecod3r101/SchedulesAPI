package org.example.ExceptionHandlers;

import com.mysql.cj.xdevapi.JsonArray;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class SchedulesExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleConstraintViolationException(
            ConstraintViolationException e) {
        List<SchedulesErrorResponse> errorResponseList = new ArrayList<>();
        SchedulesErrorResponse errorResponse = new SchedulesErrorResponse();

        for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
            errorResponse.setErrorField(constraintViolation.getPropertyPath().toString());
            errorResponse.setErrorMessage(constraintViolation.getMessage());
            errorResponseList.add(errorResponse);
        }
        return new ResponseEntity<>(errorResponseList, HttpStatus.BAD_REQUEST);
    }
}
