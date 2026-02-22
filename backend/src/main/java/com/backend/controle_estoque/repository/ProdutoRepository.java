package com.backend.controleestoque.repository;

import com.backend.controleestoque.model.Produto;
import com.backend.controleestoque.model.enums.TipoProdutoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByTipoProduto(TipoProdutoEnum tipoProduto);
}