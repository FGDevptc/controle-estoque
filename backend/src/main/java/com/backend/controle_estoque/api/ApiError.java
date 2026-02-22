package com.backend.controle_estoque.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Estrutura padrão de erro da API")
public class ApiError {

    @Schema(description = "Código interno do erro", example = "9999")
    private String codigo;

    @Schema(description = "Mensagem descritiva do erro", example = "Erro interno do servidor")
    private String mensagem;
}