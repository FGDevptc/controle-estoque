package com.backend.controle_estoque.mapper;

import com.backend.controle_estoque.dto.MovimentoRequestDTO;
import com.backend.controle_estoque.dto.MovimentoResponseDTO;
import com.backend.controle_estoque.model.MovimentoEstoque;
import com.backend.controle_estoque.model.Produto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MovimentoMapper {

    public MovimentoEstoque toEntity(MovimentoRequestDTO dto, Produto produto) {

        return MovimentoEstoque.builder()
                .produto(produto)
                .tipo(dto.tipo())
                .quantidade(dto.quantidade())
                .valorVenda(dto.valorVenda())
                .dataMovimento(LocalDateTime.now())
                .build();
    }

    public MovimentoResponseDTO toResponse(MovimentoEstoque movimento) {
        return new MovimentoResponseDTO(
                movimento.getId(),
                movimento.getTipo(),
                movimento.getQuantidade(),
                movimento.getValorVenda(),
                movimento.getDataMovimento());
    }
}