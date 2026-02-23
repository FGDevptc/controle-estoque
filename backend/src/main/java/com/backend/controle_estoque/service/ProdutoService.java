package com.backend.controle_estoque.service;

import com.backend.controle_estoque.api.PageBaseResponse;
import com.backend.controle_estoque.dto.ProdutoListagemResponseDTO;
import com.backend.controle_estoque.dto.ProdutoRequestDTO;
import com.backend.controle_estoque.dto.ProdutoResponseDTO;
import com.backend.controle_estoque.exception.BusinessException;
import com.backend.controle_estoque.exception.ResourceNotFoundException;
import com.backend.controle_estoque.mapper.ProdutoMapper;
import com.backend.controle_estoque.model.Produto;
import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import com.backend.controle_estoque.repository.ProdutoRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoMapper mapper;

    @Transactional
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {

        if (repository.existsByCodigoAndAtivoTrue(dto.codigo())) {
            throw new BusinessException("0001", "Já existe produto com esse código");
        }

        Produto produto = mapper.toEntity(dto);

        Produto salvo = repository.save(produto);

        return mapper.toResponse(salvo);
    }

    @Transactional(readOnly = true)
    public PageBaseResponse<ProdutoListagemResponseDTO> listar(
            TipoProdutoEnum tipo,
            Pageable pageable) {

        Page<ProdutoListagemResponseDTO> page = repository.listarComFiltro(tipo, pageable);

        return new PageBaseResponse<ProdutoListagemResponseDTO>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.hasNext(),
                page.hasPrevious());
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "0002",
                        "Produto não encontrado"));

        return mapper.toResponse(produto);
    }

    @Transactional
    public ProdutoResponseDTO editar(Long id, ProdutoRequestDTO dto) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "0002",
                        "Produto não encontrado"));

        if (!produto.getCodigo().equals(dto.codigo()) && produto.getAtivo()
                && repository.existsByCodigoAndAtivoTrue(dto.codigo())) {

            throw new BusinessException(
                    "0001",
                    "Já existe produto com esse código");
        }

        produto.setCodigo(dto.codigo());
        produto.setDescricao(dto.descricao());
        produto.setTipoProduto(dto.tipoProduto());
        produto.setValorFornecedor(dto.valorFornecedor());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());

        Produto atualizado = repository.save(produto);

        return mapper.toResponse(atualizado);
    }

    @Transactional
    public void deletar(Long id) {

        Produto produto = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "0002",
                        "Produto não encontrado"));

        produto.setAtivo(false);

        repository.save(produto);
    }
}