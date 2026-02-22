package com.backend.controleestoque.model;

import com.backend.controleestoque.model.enums.TipoProdutoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false)
    private String descricao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProdutoEnum tipoProduto;

    @Column(nullable = false)
    private BigDecimal valorFornecedor;

    @Column(nullable = false)
    private Integer quantidadeEstoque;
}
