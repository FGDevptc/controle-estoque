package com.backend.controle_estoque.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "Estrutura padrão de erro da API")
public class ApiError {

    @Schema(description = "Código interno do erro", example = "0002")
    private String code;

    @Schema(description = "Mensagem descritiva do erro", example = "Produto não encontrado")
    private String mensagem;
}