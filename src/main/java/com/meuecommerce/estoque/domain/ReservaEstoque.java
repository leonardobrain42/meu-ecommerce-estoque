package com.meuecommerce.estoque.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
    name = "reservas_estoque",
    uniqueConstraints = @UniqueConstraint(columnNames = {"pedido_id", "estoque_id"})
)
class ReservaEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pedido_id", nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    protected ReservaEstoque() {}

    ReservaEstoque(Estoque estoque, Long pedidoId, Integer quantidade) {
        this.estoque = estoque;
        this.pedidoId = pedidoId;
        this.quantidade = quantidade;
    }

    public Long getPedidoId() { return pedidoId; }
    public Integer getQuantidade() { return quantidade; }
}
