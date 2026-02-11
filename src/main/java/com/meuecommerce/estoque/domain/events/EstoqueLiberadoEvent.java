package com.meuecommerce.estoque.domain.events;

import java.time.Instant;

public class EstoqueLiberadoEvent {

    private final Long pedidoId;
    private final Instant ocorridoEm;

    public EstoqueLiberadoEvent(
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