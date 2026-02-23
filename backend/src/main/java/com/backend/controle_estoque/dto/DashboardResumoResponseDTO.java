package com.backend.controle_estoque.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Resumo de indicadores do dashboard")
public record DashboardResumoResponseDTO(

        @Schema(description = "Total de produtos ativos", example = "25")
        Long totalProdutos,

        @Schema(description = "Total de itens em estoque", example = "320")
        Long totalEstoque,

        @Schema(description = "Lucro total acumulado", example = "8450.00")
        BigDecimal lucroTotal
) {}
