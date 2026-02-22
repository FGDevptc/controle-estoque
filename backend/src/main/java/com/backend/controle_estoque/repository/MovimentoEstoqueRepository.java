package com.backend.controle_estoque.repository;

import com.backend.controle_estoque.model.MovimentoEstoque;
import com.backend.controle_estoque.model.enums.TipoMovimentacaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

    List<MovimentoEstoque> findByProdutoId(Long produtoId);

    List<MovimentoEstoque> findByProdutoIdAndTipo(Long produtoId, TipoMovimentacaoEnum tipo);
}