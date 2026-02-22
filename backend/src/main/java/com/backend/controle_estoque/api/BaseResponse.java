package com.backend.controleestoque.api;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BaseResponse<T> {

    private int codigoHTTP;
    private T retorno;
    private List<ApiError> erros;
}