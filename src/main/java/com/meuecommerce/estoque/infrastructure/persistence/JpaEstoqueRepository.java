package com.meuecommerce.estoque.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.meuecommerce.estoque.domain.Estoque;
import com.meuecommerce.estoque.domain.EstoqueRepository;

interface SpringDataEstoqueRepository
        extends JpaRepository<Estoque, Long> {

    Optional<Estoque> findBySku(String sku);
}

@Repository
public class JpaEstoqueRepository implements EstoqueRepository {

    private final SpringDataEstoqueRepository jpa;

    public JpaEstoqueRepository(SpringDataEstoqueRepository jpa) {
        this.jpa = jpa;
    }
    
    @Override
    public List<Estoque> findAll() {
        return jpa.findAll();
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpa.findBySku(sku).isPresent();    
    }

    @Override
    public void delete(Estoque estoque) {
        jpa.delete(estoque);
    }

    @Override
    public Estoque save(Estoque estoque) {
        return jpa.save(estoque);
    }

    @Override
    public Optional<Estoque> findBySku(String sku) {
        return jpa.findBySku(sku);
    }

    @Override
    public Optional<Estoque> findById(Long id) {
        return jpa.findById(id);
    }

}