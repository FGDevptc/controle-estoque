package com.backend.controleestoque.repository;

import com.backend.controleestoque.model.MovimentoEstoque;
import com.backend.controleestoque.model.enums.TipoMovimentacaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {

    List<MovimentoEstoque> findByProdutoId(Long produtoId);

    List<MovimentoEstoque> findByProdutoIdAndTipo(Long produtoId, TipoMovimentacaoEnum tipo);
}