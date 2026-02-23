package com.backend.controle_estoque.service;

import com.backend.controle_estoque.dto.DashboardResumoResponseDTO;
import com.backend.controle_estoque.repository.MovimentoEstoqueRepository;
import com.backend.controle_estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final ProdutoRepository produtoRepository;
    private final MovimentoEstoqueRepository movimentoRepository;

    @Transactional(readOnly = true)
    public DashboardResumoResponseDTO resumo() {
        long totalProdutos = produtoRepository.countByAtivoTrue();
        long totalEstoque = produtoRepository.somarQuantidadeEstoqueAtivo();

        return new DashboardResumoResponseDTO(
                totalProdutos,
                totalEstoque,
                movimentoRepository.calcularLucroTotal());
    }
}
