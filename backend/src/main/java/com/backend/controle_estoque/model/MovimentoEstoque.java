package com.backend.controleestoque.model;

import com.backend.controleestoque.model.enums.TipoMovimentacaoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private Produto produto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoMovimentacaoEnum tipo;

    @Column(nullable = false)
    private Integer quantidade;

    private BigDecimal valorVenda;

    @Column(nullable = false)
    private LocalDateTime dataMovimento;
}