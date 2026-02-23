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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository repository;

    @Mock
    private ProdutoMapper mapper;

    @InjectMocks
    private ProdutoService service;

    @Test
    void deveLancarExcecaoAoCriarProdutoComCodigoDuplicado() {
        ProdutoRequestDTO dto = novoProdutoRequest("TV01");
        when(repository.existsByCodigoAndAtivoTrue("TV01")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.criar(dto));

        assertEquals("0001", exception.getCode());
        verify(repository, never()).save(any());
    }

    @Test
    void deveCriarProdutoComSucesso() {
        ProdutoRequestDTO dto = novoProdutoRequest("TV01");
        Produto entity = novoProdutoEntity(1L, "TV01");
        ProdutoResponseDTO expected = novoProdutoResponse(1L, "TV01");

        when(repository.existsByCodigoAndAtivoTrue("TV01")).thenReturn(false);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(expected);

        ProdutoResponseDTO response = service.criar(dto);

        assertEquals(expected, response);
        verify(repository).save(entity);
    }

    @Test
    void deveListarProdutosComPaginacao() {
        Pageable pageable = PageRequest.of(0, 5);
        ProdutoListagemResponseDTO item = new ProdutoListagemResponseDTO(
                1L,
                "TV01",
                "Smart TV",
                TipoProdutoEnum.ELETRONICO,
                BigDecimal.valueOf(1500),
                10,
                10,
                25L
        );
        Page<ProdutoListagemResponseDTO> page = new PageImpl<>(List.of(item), pageable, 1);

        when(repository.listarComFiltro(TipoProdutoEnum.ELETRONICO, pageable)).thenReturn(page);

        PageBaseResponse<ProdutoListagemResponseDTO> response = service.listar(TipoProdutoEnum.ELETRONICO, pageable);

        assertEquals(1, response.content().size());
        assertEquals(0, response.page());
        assertEquals(5, response.size());
        assertEquals(1, response.totalElements());
    }

    @Test
    void deveLancarExcecaoQuandoBuscarProdutoInexistentePorId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> service.buscarPorId(1L));

        assertEquals("0002", exception.getCode());
    }

    @Test
    void deveLancarExcecaoAoEditarProdutoInexistente() {
        ProdutoRequestDTO dto = novoProdutoRequest("TV02");
        when(repository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(ResourceNotFoundException.class, () -> service.editar(1L, dto));

        assertEquals("0002", exception.getCode());
    }

    @Test
    void deveLancarExcecaoAoEditarComNovoCodigoDuplicado() {
        Produto produtoExistente = novoProdutoEntity(1L, "TV01");
        produtoExistente.setAtivo(true);
        ProdutoRequestDTO dto = novoProdutoRequest("TV02");

        when(repository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(repository.existsByCodigoAndAtivoTrue("TV02")).thenReturn(true);

        BusinessException exception = assertThrows(BusinessException.class, () -> service.editar(1L, dto));

        assertEquals("0001", exception.getCode());
        verify(repository, never()).save(any());
    }

    @Test
    void deveEditarProdutoComSucesso() {
        Produto produtoExistente = novoProdutoEntity(1L, "TV01");
        ProdutoRequestDTO dto = new ProdutoRequestDTO(
                "TV01",
                "Smart TV 55",
                TipoProdutoEnum.ELETRONICO,
                BigDecimal.valueOf(1700),
                20
        );
        ProdutoResponseDTO expected = new ProdutoResponseDTO(
                1L,
                "TV01",
                "Smart TV 55",
                TipoProdutoEnum.ELETRONICO,
                BigDecimal.valueOf(1700),
                20
        );

        when(repository.findById(1L)).thenReturn(Optional.of(produtoExistente));
        when(repository.save(produtoExistente)).thenReturn(produtoExistente);
        when(mapper.toResponse(produtoExistente)).thenReturn(expected);

        ProdutoResponseDTO response = service.editar(1L, dto);

        assertEquals(expected, response);
        assertEquals("Smart TV 55", produtoExistente.getDescricao());
        assertEquals(0, BigDecimal.valueOf(1700).compareTo(produtoExistente.getValorFornecedor()));
        assertEquals(20, produtoExistente.getQuantidadeEstoque());
    }

    @Test
    void deveInativarProdutoAoDeletar() {
        Produto produto = novoProdutoEntity(1L, "TV01");
        produto.setAtivo(true);

        when(repository.findById(1L)).thenReturn(Optional.of(produto));

        service.deletar(1L);

        assertFalse(produto.getAtivo());
        verify(repository).save(produto);
    }

    private ProdutoRequestDTO novoProdutoRequest(String codigo) {
        return new ProdutoRequestDTO(
                codigo,
                "Smart TV",
                TipoProdutoEnum.ELETRONICO,
                BigDecimal.valueOf(1500),
                10
        );
    }

    private Produto novoProdutoEntity(Long id, String codigo) {
        return Produto.builder()
                .id(id)
                .codigo(codigo)
                .descricao("Smart TV")
                .tipoProduto(TipoProdutoEnum.ELETRONICO)
                .valorFornecedor(BigDecimal.valueOf(1500))
                .quantidadeEstoque(10)
                .ativo(true)
                .build();
    }

    private ProdutoResponseDTO novoProdutoResponse(Long id, String codigo) {
        return new ProdutoResponseDTO(
                id,
                codigo,
                "Smart TV",
                TipoProdutoEnum.ELETRONICO,
                BigDecimal.valueOf(1500),
                10
        );
    }
}
