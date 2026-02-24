package com.backend.controle_estoque.repository;

import com.backend.controle_estoque.model.MovimentoEstoque;
import com.backend.controle_estoque.model.enums.TipoMovimentacaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

    List<MovimentoEstoque> findByProdutoId(Long produtoId);

    List<MovimentoEstoque> findByProdutoIdAndTipo(Long produtoId, TipoMovimentacaoEnum tipo);

    @Query("""
                SELECT
                    COALESCE(SUM(m.valorVenda), 0)
                    - COALESCE(SUM(p.valorFornecedor * m.quantidade), 0)
                FROM MovimentoEstoque m
                JOIN m.produto p
                WHERE m.tipo = com.backend.controle_estoque.model.enums.TipoMovimentacaoEnum.SAIDA
                  AND p.ativo = true
            """)
    BigDecimal calcularLucroTotal();
}
