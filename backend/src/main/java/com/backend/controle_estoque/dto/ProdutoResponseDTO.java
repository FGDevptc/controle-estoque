package com.backend.controle_estoque.dto;

import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO de resposta de produto")
public record ProdutoResponseDTO(

        @Schema(description = "ID do produto", example = "1")
        Long id,

        @Schema(description = "Código do produto", example = "TV01")
        String codigo,

        @Schema(description = "Descrição do produto", example = "Smart TV 50 polegadas")
        String descricao,

        @Schema(description = "Tipo do produto", example = "ELETRONICO")
        TipoProdutoEnum tipoProduto,

        @Schema(description = "Valor de compra no fornecedor", example = "1500.00")
        BigDecimal valorFornecedor,

        @Schema(description = "Quantidade disponível em estoque", example = "10")
        Integer quantidadeEstoque
) {}