package com.meuecommerce.estoque.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meuecommerce.estoque.application.ports.EstoqueEventPublisher;
import com.meuecommerce.estoque.domain.Estoque;
import com.meuecommerce.estoque.domain.EstoqueRepository;
import com.meuecommerce.estoque.domain.events.ItemPedidoEvent;
import com.meuecommerce.estoque.domain.events.PedidoCriadoEvent;
import com.meuecommerce.estoque.exceptions.EstoqueNaoEncontradoException;

@Service
public class EstoqueService {

    private final EstoqueRepository repository;
    private final EstoqueEventPublisher publisher;

    public EstoqueService(
        EstoqueRepository repository,
        EstoqueEventPublisher publisher
    ) {
        this.publisher = publisher;
        this.repository = repository;
    }

    @Transactional
    public Estoque criarEstoque(String sku, String descricao, Integer quantidade, Integer quantidadeMinima) {
        repository.findBySku(sku).ifPresent(e -> {
            throw new IllegalArgumentException("SKU já existe no estoque");
        });

        Estoque estoque = new Estoque(sku, descricao, quantidade, quantidadeMinima);
        return repository.save(estoque);
    }

    @Transactional
    public Estoque adicionarQuantidadePorSku(String sku, Integer quantidade) {
        Estoque estoque = buscarPorSku(sku);
        estoque.adicionarQuantidade(quantidade);
        return repository.save(estoque);
    }

    @Transactional
    public void reservarEstoque(PedidoCriadoEvent event) {
        
    List<ItemPedidoEvent> itensReservados = new ArrayList<>();

    for (ItemPedidoEvent item : event.itens()) {

        Estoque estoque = buscarPorSku(item.sku());

        boolean reservado = estoque.reservar(
                event.pedidoId(),
                item.quantidade()
        );

        if (!reservado) {
            // compensação
            for (ItemPedidoEvent reservadoItem : itensReservados) {
                Estoque estoqueCompensar = buscarPorSku(reservadoItem.sku());
                estoqueCompensar.liberarReserva(
                        event.pedidoId()
                );
            }

            publisher.estoqueFalhaReserva(
                event.pedidoId()
            );

            return;
        }

        itensReservados.add(item);

        publisher.estoqueReservado(event.pedidoId());
    }

    // só publica sucesso quando TODOS foram reservados
    publisher.estoqueReservado(
        event.pedidoId()
    );
    }

    @Transactional
    public void liberarReserva(Long pedidoId, String sku, Integer quantidade) {
        Estoque estoque = buscarPorSku(sku);
        estoque.liberarReserva(pedidoId);

        publisher.estoqueLiberado(pedidoId);
    }

    @Transactional
    public void confirmarReserva(Long pedidoId, String sku, Integer quantidade) {
        Estoque estoque = buscarPorSku(sku);
        estoque.confirmarReserva(pedidoId);

        publisher.estoqueBaixado(
            pedidoId,
            sku,
            quantidade
        );
    }

    @Transactional(readOnly = true)
    public boolean estaAbaixoDaMinima(String sku) {
        return buscarPorSku(sku).estaAbaixoDaMinima();
    }

    // @Transactional
    // public Estoque atualizar(String sku, String descricao, Integer quantidadeMinima) {
    //     Estoque estoque = buscarPorSku(sku);
    //     estoque.atualizarDescricao(descricao);
    //     estoque.atualizarQuantidadeMinima(quantidadeMinima);
    //     return repository.save(estoque);
    // }

    @Transactional(readOnly = true)
    public List<Estoque> listarTodos() {
        return repository.findAll();
    }

    @Transactional
    public void deletarPorSku(String sku) {
        Estoque estoque = buscarPorSku(sku);
        repository.delete(estoque);
    }

    private Estoque buscarPorSku(String sku) {
        return repository.findBySku(sku)
            .orElseThrow(() ->
                new EstoqueNaoEncontradoException("Estoque com SKU " + sku + " não encontrado")
            );
    }
}