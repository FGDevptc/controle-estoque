package com.backend.controle_estoque.dto;

import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de listagem de produto com agregação de saída")
public record ProdutoListagemResponseDTO(

        @Schema(example = "1")
        Long id,

        @Schema(example = "TV01")
        String codigo,

        @Schema(example = "Smart TV")
        String descricao,

        TipoProdutoEnum tipoProduto,

        @Schema(description = "Quantidade disponível em estoque", example = "10")
        Integer quantidadeDisponivel,

        @Schema(description = "Quantidade total de saída", example = "25")
        Long quantidadeTotalSaida
) {}