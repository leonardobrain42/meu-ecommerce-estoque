package com.meuecommerce.estoque.domain.events;

import java.time.Instant;

public class EstoqueBaixadoEvent {

    private final String pedidoId;
    private final String sku;
    private final Integer quantidade;
    private final Instant ocorridoEm;

    public EstoqueBaixadoEvent(
        String pedidoId,
        String sku,
        Integer quantidade
    ) {
        this.pedidoId = pedidoId;
        this.sku = sku;
        this.quantidade = quantidade;
        this.ocorridoEm = Instant.now();
    }

    public String getPedidoId() {
        return pedidoId;
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Instant getOcorridoEm() {
        return ocorridoEm;
    }
}