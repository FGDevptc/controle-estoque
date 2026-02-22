package com.backend.controle_estoque.dto;

import com.backend.controle_estoque.model.enums.TipoMovimentacaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "Movimentação de estoque")
public record MovimentoRequestDTO(

        @Schema(description = "ID do produto", example = "1")
        @NotNull(message = "ID do produto é obrigatório")
        Long produtoId,

        @Schema(description = "Tipo da movimentação", example = "ENTRADA")
        @NotNull(message = "Tipo da movimentação é obrigatório")
        TipoMovimentacaoEnum tipo,

        @Schema(description = "Quantidade movimentada", example = "5")
        @NotNull(message = "Quantidade é obrigatória")
        @Positive(message = "Quantidade deve ser maior que zero")
        Integer quantidade,

        @Schema(description = "Valor de venda (obrigatório para SAIDA)", example = "2000.00")
        @Positive(message = "Valor de venda deve ser maior que zero")
        BigDecimal valorVenda
) {}