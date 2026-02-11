package com.meuecommerce.estoque.domain.events;

import java.time.Instant;

public class EstoqueBaixadoEvent {

    private final Long pedidoId;
    private final Instant ocorridoEm;

    public EstoqueBaixadoEvent(
        Long pedidoId
    ) {
        this.pedidoId = pedidoId;
        this.ocorridoEm = Instant.now();
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Instant getOcorridoEm() {
        return ocorridoEm;
    }
}