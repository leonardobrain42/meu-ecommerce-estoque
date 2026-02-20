package com.meuecommerce.estoque.application.ports;

import com.meuecommerce.estoque.domain.events.EstoqueBaixadoEvent;
import com.meuecommerce.estoque.domain.events.EstoqueFalhaReservaEvent;
import com.meuecommerce.estoque.domain.events.EstoqueLiberadoEvent;
import com.meuecommerce.estoque.domain.events.EstoqueReservadoEvent;

public interface EstoqueEventPublisher {
     void estoqueReservado(EstoqueReservadoEvent event);

    void estoqueFalhaReserva(EstoqueFalhaReservaEvent event);

    void estoqueLiberado(EstoqueLiberadoEvent event);

    void estoqueBaixado(EstoqueBaixadoEvent event);
}