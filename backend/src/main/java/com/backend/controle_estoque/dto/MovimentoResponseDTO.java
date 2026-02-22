package com.backend.controle_estoque.dto;

import com.backend.controle_estoque.model.enums.TipoMovimentacaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "DTO de resposta da movimentação de estoque")
public record MovimentoResponseDTO(

        @Schema(description = "ID da movimentação", example = "1")
        Long id,

        @Schema(description = "Tipo da movimentação", example = "ENTRADA")
        TipoMovimentacaoEnum tipo,

        @Schema(description = "Quantidade movimentada", example = "5")
        Integer quantidade,

        @Schema(description = "Valor de venda (quando aplicável)", example = "2500.00")
        BigDecimal valorVenda,

        @Schema(description = "Data da movimentação")
        LocalDateTime dataMovimento
) {}