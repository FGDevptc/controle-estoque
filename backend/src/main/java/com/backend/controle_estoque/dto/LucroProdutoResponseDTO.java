package com.backend.controle_estoque.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO de cálculo de lucro por produto")
public record LucroProdutoResponseDTO(

        @Schema(description = "ID do produto", example = "1")
        Long produtoId,

        @Schema(description = "Lucro total acumulado", example = "3500.00")
        BigDecimal lucroTotal
) {}