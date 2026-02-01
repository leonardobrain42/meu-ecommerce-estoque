package com.meuecommerce.estoque.domain;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "estoques", uniqueConstraints = {
    @UniqueConstraint(columnNames = "sku")
})
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String sku;

    @Column(nullable = true)
    private String descricao;

    @Column(nullable = false)
    private Integer quantidadeTotal;

    @Column(nullable = false)
    private Integer quantidadeMinima;

    @OneToMany(
        mappedBy = "estoque",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private Set<ReservaEstoque> reservas = new HashSet<>();

    protected Estoque() {
        // JPA
    }

    public Estoque(String sku, String descricao, Integer quantidadeInicial, Integer quantidadeMinima) {
        validarCriacao(sku, quantidadeInicial, quantidadeMinima);
        this.sku = sku;
        this.descricao = descricao;
        this.quantidadeTotal = quantidadeInicial;
        this.quantidadeMinima = quantidadeMinima;
    }

    /* ======================
       Comportamento de domínio
       ====================== */

    public void reservar(String pedidoId, int quantidade) {
        validarQuantidade(quantidade);

        if (quantidadeDisponivel() < quantidade) {
            throw new IllegalStateException("Estoque insuficiente para reserva");
        }

        boolean jaReservado = reservas.stream()
            .anyMatch(r -> r.getPedidoId().equals(pedidoId));

        if (jaReservado) {
            throw new IllegalStateException("Pedido já possui reserva");
        }

        reservas.add(new ReservaEstoque(this, pedidoId, quantidade));
    }

    public void confirmarReserva(String pedidoId) {
        ReservaEstoque reserva = obterReserva(pedidoId);

        this.quantidadeTotal -= reserva.getQuantidade();
        reservas.remove(reserva);
    }

    public void liberarReserva(String pedidoId) {
        ReservaEstoque reserva = obterReserva(pedidoId);
        reservas.remove(reserva);
    }

    public int quantidadeDisponivel() {
        return quantidadeTotal - quantidadeReservada();
    }

    public boolean estaAbaixoDaMinima() {
        return quantidadeTotal < quantidadeMinima;
    }

    public void adicionarQuantidade(int quantidade) {
        validarQuantidade(quantidade);
        this.quantidadeTotal += quantidade;
    }

    /* ======================
       Regras internas
       ====================== */

    private int quantidadeReservada() {
        return reservas.stream()
            .mapToInt(ReservaEstoque::getQuantidade)
            .sum();
    }

    private ReservaEstoque obterReserva(String pedidoId) {
        return reservas.stream()
            .filter(r -> r.getPedidoId().equals(pedidoId))
            .findFirst()
            .orElseThrow(() ->
                new IllegalStateException("Reserva não encontrada para o pedido " + pedidoId)
            );
    }

    private void validarCriacao(String sku, Integer qtd, Integer qtdMin) {
        if (sku == null || sku.isBlank()) {
            throw new IllegalArgumentException("SKU é obrigatório");
        }
        if (qtd == null || qtd < 0) {
            throw new IllegalArgumentException("Quantidade inicial inválida");
        }
        if (qtdMin == null || qtdMin < 0) {
            throw new IllegalArgumentException("Quantidade mínima inválida");
        }
    }

    private void validarQuantidade(Integer qtd) {
        if (qtd == null || qtd <= 0) {
            throw new IllegalArgumentException("Quantidade deve ser maior que zero");
        }
    }

    /* ======================
       Getters
       ====================== */

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getDescricao() { return descricao; }
    public Integer getQuantidadeTotal() { return quantidadeTotal; }
    public Integer getQuantidadeMinima() { return quantidadeMinima; }
}