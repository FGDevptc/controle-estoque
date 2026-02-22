package com.backend.controle_estoque.dto;

import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

@Schema(description = "DTO para criação de produto")
public record ProdutoRequestDTO(

        @Schema(description = "Código único do produto", example = "TV01")
        @NotBlank(message = "Código é obrigatório")
        String codigo,

        @Schema(description = "Descrição do produto", example = "Smart TV 50 polegadas")
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @Schema(description = "Tipo do produto", example = "ELETRONICO")
        @NotNull(message = "Tipo é obrigatório")
        TipoProdutoEnum tipoProduto,

        @Schema(description = "Valor de compra no fornecedor", example = "1500.00")
        @NotNull
        @Positive
        BigDecimal valorFornecedor,

        @Schema(description = "Quantidade inicial em estoque", example = "10")
        @NotNull
        @Min(0)
        Integer quantidadeEstoque
) {}