package com.backend.controle_estoque.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Schema(description = "Estrutura padrão de resposta da API")
public class BaseResponse<T> {

    @Schema(description = "Código HTTP da resposta", example = "200")
    private int codigoHTTP;

    @Schema(description = "Objeto de retorno da requisição")
    private T retorno;

    @Schema(description = "Lista de erros da requisição")
    private List<ApiError> erros;
}