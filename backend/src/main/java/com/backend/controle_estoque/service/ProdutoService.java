package com.backend.controle_estoque.service;

import com.backend.controle_estoque.dto.ProdutoRequestDTO;
import com.backend.controle_estoque.dto.ProdutoResponseDTO;
import com.backend.controle_estoque.exception.BusinessException;
import com.backend.controle_estoque.mapper.ProdutoMapper;
import com.backend.controle_estoque.model.Produto;
import com.backend.controle_estoque.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {

        if (repository.existsByCodigo(dto.codigo())) {
            throw new BusinessException("0001", "Já existe produto com esse código");
        }

        Produto produto = mapper.toEntity(dto);

        Produto salvo = repository.save(produto);

        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listar() {

        return repository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}