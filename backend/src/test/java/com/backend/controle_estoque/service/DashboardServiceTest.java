package com.backend.controle_estoque.service;

import com.backend.controle_estoque.dto.DashboardResumoResponseDTO;
import com.backend.controle_estoque.repository.MovimentoEstoqueRepository;
import com.backend.controle_estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentoEstoqueRepository movimentoRepository;

    @InjectMocks
    private DashboardService service;

    @Test
    void deveRetornarResumoDoDashboard() {
        when(produtoRepository.countByAtivoTrue()).thenReturn(3L);
        when(produtoRepository.somarQuantidadeEstoqueAtivo()).thenReturn(25L);
        when(movimentoRepository.calcularLucroTotal()).thenReturn(BigDecimal.valueOf(4200));

        DashboardResumoResponseDTO response = service.resumo();

        assertEquals(3L, response.totalProdutos());
        assertEquals(25L, response.totalEstoque());
        assertEquals(0, BigDecimal.valueOf(4200).compareTo(response.lucroTotal()));

        verify(produtoRepository).countByAtivoTrue();
        verify(produtoRepository).somarQuantidadeEstoqueAtivo();
        verify(movimentoRepository).calcularLucroTotal();
    }
}
