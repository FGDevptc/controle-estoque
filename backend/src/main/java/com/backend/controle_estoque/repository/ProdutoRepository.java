package com.backend.controle_estoque.repository;

import com.backend.controle_estoque.dto.ProdutoListagemResponseDTO;
import com.backend.controle_estoque.model.Produto;
import com.backend.controle_estoque.model.enums.TipoProdutoEnum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("""
                SELECT new com.backend.controle_estoque.dto.ProdutoListagemResponseDTO(
                    p.id,
                    p.codigo,
                    p.descricao,
                    p.tipoProduto,
                    p.quantidadeEstoque,
                    COALESCE(SUM(m.quantidade), 0)
                )
                FROM Produto p
                LEFT JOIN MovimentoEstoque m
                    ON m.produto.id = p.id
                    AND m.tipo = 'SAIDA'
                WHERE p.ativo = true
                  AND (:tipo IS NULL OR p.tipoProduto = :tipo)
                GROUP BY p.id
            """)
    Page<ProdutoListagemResponseDTO> listarComFiltro(
            TipoProdutoEnum tipo,
            Pageable pageable);

    List<Produto> findByTipoProduto(TipoProdutoEnum tipoProduto);

    boolean existsByCodigo(String codigo);
}