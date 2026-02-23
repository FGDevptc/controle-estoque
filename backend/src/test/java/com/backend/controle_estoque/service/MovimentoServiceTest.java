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
import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import com.backend.controle_estoque.repository.MovimentoEstoqueRepository;
import com.backend.controle_estoque.repository.ProdutoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimentoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private MovimentoEstoqueRepository movimentoRepository;

    @Mock
    private MovimentoMapper mapper;

    @InjectMocks
    private MovimentoService service;

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExisteNaMovimentacao() {
        MovimentoRequestDTO dto = novaMovimentacaoSaida(1L, 2, BigDecimal.valueOf(2000));
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> service.movimentar(dto));

        assertEquals("0002", exception.getCode());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoEstaInativo() {
        Produto produto = novoProduto(1L, 10, false, BigDecimal.valueOf(1500));
        MovimentoRequestDTO dto = novaMovimentacaoSaida(1L, 2, BigDecimal.valueOf(2000));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.movimentar(dto));

        assertEquals("0004", exception.getCode());
        verify(movimentoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoEmSaidaComSaldoInsuficiente() {
        Produto produto = novoProduto(1L, 1, true, BigDecimal.valueOf(1500));
        MovimentoRequestDTO dto = novaMovimentacaoSaida(1L, 2, BigDecimal.valueOf(2000));
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.movimentar(dto));

        assertEquals("0003", exception.getCode());
        verify(movimentoRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoEmSaidaSemValorVenda() {
        Produto produto = novoProduto(1L, 10, true, BigDecimal.valueOf(1500));
        MovimentoRequestDTO dto = novaMovimentacaoSaida(1L, 2, null);
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        BusinessException exception = assertThrows(BusinessException.class, () -> service.movimentar(dto));

        assertEquals("0005", exception.getCode());
        verify(movimentoRepository, never()).save(any());
    }

    @Test
    void deveMovimentarSaidaComSucesso() {
        Produto produto = novoProduto(1L, 10, true, BigDecimal.valueOf(1500));
        MovimentoRequestDTO dto = novaMovimentacaoSaida(1L, 4, BigDecimal.valueOf(2200));
        MovimentoEstoque movimento = new MovimentoEstoque();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(mapper.toEntity(dto, produto)).thenReturn(movimento);

        service.movimentar(dto);

        assertEquals(6, produto.getQuantidadeEstoque());
        verify(movimentoRepository).save(movimento);
    }

    @Test
    void deveMovimentarEntradaComSucesso() {
        Produto produto = novoProduto(1L, 10, true, BigDecimal.valueOf(1500));
        MovimentoRequestDTO dto = new MovimentoRequestDTO(
                1L,
                TipoMovimentacaoEnum.ENTRADA,
                3,
                null
        );
        MovimentoEstoque movimento = new MovimentoEstoque();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(mapper.toEntity(dto, produto)).thenReturn(movimento);

        service.movimentar(dto);

        assertEquals(13, produto.getQuantidadeEstoque());
        verify(movimentoRepository).save(movimento);
    }

    @Test
    void deveLancarExcecaoAoListarMovimentacoesDeProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> service.listarPorProduto(1L));

        assertEquals("0002", exception.getCode());
    }

    @Test
    void deveListarMovimentacoesPorProduto() {
        Produto produto = novoProduto(1L, 10, true, BigDecimal.valueOf(1500));
        MovimentoEstoque movimento = MovimentoEstoque.builder()
                .id(10L)
                .produto(produto)
                .tipo(TipoMovimentacaoEnum.ENTRADA)
                .quantidade(3)
                .valorVenda(null)
                .dataMovimento(LocalDateTime.now())
                .build();
        MovimentoResponseDTO expected = new MovimentoResponseDTO(
                10L,
                TipoMovimentacaoEnum.ENTRADA,
                3,
                null,
                movimento.getDataMovimento()
        );

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentoRepository.findByProdutoId(1L)).thenReturn(List.of(movimento));
        when(mapper.toResponse(movimento)).thenReturn(expected);

        List<MovimentoResponseDTO> response = service.listarPorProduto(1L);

        assertEquals(1, response.size());
        assertEquals(expected, response.get(0));
    }

    @Test
    void deveLancarExcecaoAoCalcularLucroDeProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> service.calcularLucro(1L));

        assertEquals("0002", exception.getCode());
    }

    @Test
    void deveCalcularLucroPelaFormulaEsperada() {
        Produto produto = novoProduto(1L, 10, true, BigDecimal.valueOf(1500));

        MovimentoEstoque saida1 = MovimentoEstoque.builder()
                .id(1L)
                .produto(produto)
                .tipo(TipoMovimentacaoEnum.SAIDA)
                .quantidade(2)
                .valorVenda(BigDecimal.valueOf(2000))
                .dataMovimento(LocalDateTime.now())
                .build();

        MovimentoEstoque saida2 = MovimentoEstoque.builder()
                .id(2L)
                .produto(produto)
                .tipo(TipoMovimentacaoEnum.SAIDA)
                .quantidade(3)
                .valorVenda(BigDecimal.valueOf(1900))
                .dataMovimento(LocalDateTime.now())
                .build();

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(movimentoRepository.findByProdutoIdAndTipo(1L, TipoMovimentacaoEnum.SAIDA))
                .thenReturn(List.of(saida1, saida2));

        LucroProdutoResponseDTO response = service.calcularLucro(1L);

        assertEquals(1L, response.produtoId());
        assertEquals(0, BigDecimal.valueOf(2200).compareTo(response.lucroTotal()));
    }

    private Produto novoProduto(Long id, Integer quantidade, boolean ativo, BigDecimal valorFornecedor) {
        return Produto.builder()
                .id(id)
                .codigo("TV01")
                .descricao("Smart TV")
                .tipoProduto(TipoProdutoEnum.ELETRONICO)
                .valorFornecedor(valorFornecedor)
                .quantidadeEstoque(quantidade)
                .ativo(ativo)
                .build();
    }

    private MovimentoRequestDTO novaMovimentacaoSaida(Long produtoId, Integer quantidade, BigDecimal valorVenda) {
        return new MovimentoRequestDTO(
                produtoId,
                TipoMovimentacaoEnum.SAIDA,
                quantidade,
                valorVenda
        );
    }
}
