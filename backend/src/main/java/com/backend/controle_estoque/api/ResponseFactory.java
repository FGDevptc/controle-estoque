package com.backend.controle_estoque.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseFactory {

    public static <T> ResponseEntity<BaseResponse<T>> success(T data) {
        BaseResponse<T> response = BaseResponse.<T>builder()
                .codigoHTTP(HttpStatus.OK.value())
                .retorno(data)
                .erros(null)
                .build();

        return ResponseEntity.ok(response);
    }

    public static ResponseEntity<BaseResponse<Object>> error(HttpStatus status, String code, String message) {

        ApiError error = ApiError.builder()
                .codigo(code)
                .mensagem(message)
                .build();

        BaseResponse<Object> response = BaseResponse.builder()
                .codigoHTTP(status.value())
                .retorno(null)
                .erros(List.of(error))
                .build();

        return ResponseEntity.status(status).body(response);
    }
}