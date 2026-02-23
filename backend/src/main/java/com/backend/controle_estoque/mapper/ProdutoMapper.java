package com.backend.controle_estoque.mapper;

import com.backend.controle_estoque.dto.ProdutoRequestDTO;
import com.backend.controle_estoque.dto.ProdutoResponseDTO;
import com.backend.controle_estoque.model.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto) {
        return Produto.builder()
                .codigo(dto.codigo())
                .descricao(dto.descricao())
                .tipoProduto(dto.tipoProduto())
                .valorFornecedor(dto.valorFornecedor())
                .quantidadeEstoque(dto.quantidadeEstoque())
                .ativo(true)
                .build();
    }

    public ProdutoResponseDTO toResponse(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getCodigo(),
                produto.getDescricao(),
                produto.getTipoProduto(),
                produto.getValorFornecedor(),
                produto.getQuantidadeEstoque()
        );
    }
}