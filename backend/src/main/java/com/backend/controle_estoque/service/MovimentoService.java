package com.backend.controle_estoque.service;

import com.backend.controle_estoque.dto.LucroProdutoResponseDTO;
import com.backend.controle_estoque.dto.MovimentoRequestDTO;
import com.backend.controle_estoque.dto.MovimentoResponseDTO;
import com.backend.controle_estoque.exception.BusinessException;
import com.backend.controle_estoque.exception.ResourceNotFoundException;
import com.backend.controle_estoque.mapper.MovimentoMapper;
import com.backend.controle_estoque.model.MovimentoEstoque;
import com.backend.controle_estoque.model.Produto;
import com.backend.controle_estoque.model.enums.TipoMovimentacaoEnum;
import com.backend.controle_estoque.repository.MovimentoEstoqueRepository;
import com.backend.controle_estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MovimentoService {

    private final ProdutoRepository produtoRepository;
    private final MovimentoEstoqueRepository movimentoRepository;
    private final MovimentoMapper mapper;

    @Transactional
    public void movimentar(MovimentoRequestDTO dto) {

        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException("0002", "Produto não encontrado"));

        if (!produto.getAtivo()) {
            throw new BusinessException("0004", "Produto inativo");
        }

        if (dto.tipo() == TipoMovimentacaoEnum.SAIDA) {

            if (produto.getQuantidadeEstoque() < dto.quantidade()) {
                throw new BusinessException("0003", "Saldo insuficiente em estoque");
            }

            if (dto.valorVenda() == null) {
                throw new BusinessException("0005", "Valor de venda é obrigatório para saída");
            }

            produto.setQuantidadeEstoque(
                    produto.getQuantidadeEstoque() - dto.quantidade());

        } else {

            produto.setQuantidadeEstoque(
                    produto.getQuantidadeEstoque() + dto.quantidade());
        }

        MovimentoEstoque movimento = mapper.toEntity(dto, produto);

        movimentoRepository.save(movimento);
    }

    @Transactional(readOnly = true)
    public List<MovimentoResponseDTO> listarPorProduto(Long produtoId) {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("0002", "Produto não encontrado"));

        return movimentoRepository.findByProdutoId(produtoId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public LucroProdutoResponseDTO calcularLucro(Long produtoId) {

        Produto produto = produtoRepository.findById(produtoId)
                .orElseThrow(() -> new ResourceNotFoundException("0002", "Produto não encontrado"));

        List<MovimentoEstoque> saidas = movimentoRepository.findByProdutoIdAndTipo(
                produtoId,
                TipoMovimentacaoEnum.SAIDA);

        BigDecimal lucroTotal = saidas.stream()
                .map(mov -> {
                    BigDecimal lucroUnitario = mov.getValorVenda().subtract(produto.getValorFornecedor());

                    return lucroUnitario.multiply(
                            BigDecimal.valueOf(mov.getQuantidade()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new LucroProdutoResponseDTO(produtoId, lucroTotal);
    }
}