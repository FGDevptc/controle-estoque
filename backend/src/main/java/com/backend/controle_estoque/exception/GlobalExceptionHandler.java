package com.backend.controle_estoque.exception;

import com.backend.controle_estoque.api.ApiError;
import com.backend.controle_estoque.api.BaseResponse;
import com.backend.controle_estoque.api.ResponseFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Object handleBusiness(BusinessException ex) {
        return ResponseFactory.error(
                HttpStatus.BAD_REQUEST,
                ex.getCode(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public Object handleNotFound(ResourceNotFoundException ex) {
        return ResponseFactory.error(
                HttpStatus.NOT_FOUND,
                ex.getCode(),
                ex.getMessage()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidation(MethodArgumentNotValidException ex) {

        List<ApiError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> ApiError.builder()
                        .codigo("VALIDATION_ERROR")
                        .mensagem(fieldError.getField() + ": " + fieldError.getDefaultMessage())
                        .build())
                .collect(Collectors.toList());

        BaseResponse<Object> response = BaseResponse.builder()
                .codigoHTTP(HttpStatus.BAD_REQUEST.value())
                .retorno(null)
                .erros(errors)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneric(Exception ex) {
        return ResponseFactory.error(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "9999",
                "Erro interno do servidor"
        );
    }
}