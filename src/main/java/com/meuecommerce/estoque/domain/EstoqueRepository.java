package com.meuecommerce.estoque.domain;

import java.util.List;
import java.util.Optional;

public interface EstoqueRepository {

    Estoque save(Estoque estoque);

    Optional<Estoque> findById(Long id);

    Optional<Estoque> findBySku(String sku);

    List<Estoque> findAll();

    boolean existsBySku(String sku);

    void delete(Estoque estoque);
}