package com.backend.controle_estoque.repository;

import com.backend.controle_estoque.model.Produto;
import com.backend.controle_estoque.model.enums.TipoProdutoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    List<Produto> findByTipoProduto(TipoProdutoEnum tipoProduto);

    boolean existsByCodigo(String codigo);
}