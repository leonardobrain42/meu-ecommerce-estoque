package com.meuecommerce.estoque.infrastructure.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
// import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.meuecommerce.estoque.application.EstoqueService;
import com.meuecommerce.estoque.domain.Estoque;

@RestController
@CrossOrigin(origins = "*")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

//     /**
//      * Health check endpoint
//      */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "Estoque API"));
    }

//     /**
//      * Cria um novo produto no estoque
//      */
    @PostMapping("/api/estoque")
    public ResponseEntity<Estoque> criar(@RequestBody Map<String, Object> request) {
        String sku = (String) request.get("sku");
        String descricao = (String) request.get("descricao");
        Integer quantidade = ((Number) request.get("quantidade")).intValue();
        Integer quantidadeMinima = ((Number) request.get("quantidadeMinima")).intValue();
        Estoque estoque = estoqueService.criarEstoque(sku, descricao, quantidade, quantidadeMinima);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

//     /**
//      * Lista todos os produtos em estoque
//      */
    @GetMapping("/api/estoque")
    public ResponseEntity<List<Estoque>> listarTodos() {
        System.out.println("Recebido no controller - Listando todos os estoques");
        List<Estoque> estoques = estoqueService.listarTodos();
        return ResponseEntity.ok(estoques);
    }

//     /**
//      * Busca estoque por ID
//      */
//     @GetMapping("/api/estoque/{id}")
//     public ResponseEntity<Estoque> buscarPorId(@PathVariable Long id) {
//         Estoque estoque = estoqueService.buscarEstoquePorId(id);
//         return ResponseEntity.ok(estoque);
//     }

//     /**
//      * Busca estoque por SKU
//      */
//     @GetMapping("/api/estoque/sku/{sku}")
//     public ResponseEntity<Estoque> buscarPorSku(@PathVariable String sku) {
//         Estoque estoque = estoqueService.buscarEstoquePorSku(sku);
//         return ResponseEntity.ok(estoque);
//     }

//     /**
//      * Adiciona quantidade ao estoque
//      */
    // @PostMapping("/api/estoque/{sku}/adicionar")
    // public ResponseEntity<Estoque> adicionarQuantidade(
    //         @PathVariable String sku,
    //         @RequestBody Map<String, Object> request) {
    //     Integer quantidade = ((Number) request.get("quantidade")).intValue();
    //     Estoque estoque = estoqueService.adicionaQuantidade(sku, quantidade);
    //     return ResponseEntity.ok(estoque);
    // }

//     /**
//      * Adiciona quantidade ao estoque por SKU
//      */
    @PostMapping("/api/estoque/sku/{sku}/adicionar")
    public ResponseEntity<Estoque> adicionarQuantidadePorSku(
            @PathVariable String sku,
            @RequestBody Map<String, Object> request) {
        Integer quantidade = ((Number) request.get("quantidade")).intValue();
        Estoque estoque = estoqueService.adicionarQuantidadePorSku(sku, quantidade);
        return ResponseEntity.ok(estoque);
    }

//     /**
//      * Remove quantidade do estoque
//      */
//     @PostMapping("/api/estoque/{id}/remover")
//     public ResponseEntity<Estoque> removerQuantidade(
//             @PathVariable Long id,
//             @RequestBody Map<String, Object> request) {
//         Integer quantidade = ((Number) request.get("quantidade")).intValue();
//         Estoque estoque = estoqueService.removerQuantidade(id, quantidade);
//         return ResponseEntity.ok(estoque);
//     }

//     /**
//      * Remove quantidade do estoque por SKU
//      */
//     @PostMapping("/api/estoque/sku/{sku}/remover")
//     public ResponseEntity<Estoque> removerQuantidadePorSku(
//             @PathVariable String sku,
//             @RequestBody Map<String, Object> request) {
//         Integer quantidade = ((Number) request.get("quantidade")).intValue();
//         Estoque estoque = estoqueService.removerQuantidadePorSku(sku, quantidade);
//         return ResponseEntity.ok(estoque);
//     }

//     /**
//      * Verifica disponibilidade
//      */
//     @GetMapping("/api/estoque/{id}/verificar/{quantidade}")
//     public ResponseEntity<Map<String, Object>> verificarDisponibilidade(
//             @PathVariable Long id,
//             @PathVariable Integer quantidade) {
//         boolean disponivel = estoqueService.verificarDisponibilidade(id, quantidade);
//         return ResponseEntity.ok(Map.of(
//             "id", id,
//             "quantidade_solicitada", quantidade,
//             "disponivel", disponivel
//         ));
//     }

//     /**
//      * Verifica disponibilidade por SKU
//      */
//     @GetMapping("/api/estoque/sku/{sku}/verificar/{quantidade}")
//     public ResponseEntity<Map<String, Object>> verificarDisponibilidadePorSku(
//             @PathVariable String sku,
//             @PathVariable Integer quantidade) {
//         boolean disponivel = estoqueService.verificarDisponibilidadePorSku(sku, quantidade);
//         return ResponseEntity.ok(Map.of(
//             "sku", sku,
//             "quantidade_solicitada", quantidade,
//             "disponivel", disponivel
//         ));
//     }

//     /**
//      * Atualiza informações do estoque
//      */
//     @PutMapping("/api/estoque/{id}")
//     public ResponseEntity<Estoque> atualizar(
//             @PathVariable Long id,
//             @RequestBody Map<String, Object> request) {
//         String descricao = (String) request.get("descricao");
//         Integer quantidadeMinima = request.get("quantidadeMinima") != null ?
//             ((Number) request.get("quantidadeMinima")).intValue() : null;

//         Estoque estoque = estoqueService.atualizar(id, descricao, quantidadeMinima);
//         return ResponseEntity.ok(estoque);
//     }

//     /**
//      * Delete um produto do estoque
//      */
//     @DeleteMapping("/api/estoque/{id}")
//     public ResponseEntity<Void> deletar(@PathVariable Long id) {
//         estoqueService.deletarEstoque(id);
//         return ResponseEntity.noContent().build();
//     }

//     /**
//      * Verifica se está abaixo da quantidade mínima
//      */
//     @GetMapping("/api/estoque/{id}/abaixo-minima")
//     public ResponseEntity<Map<String, Object>> verificarAbaixoMinima(@PathVariable Long id) {
//         Estoque estoque = estoqueService.buscarEstoquePorId(id);
//         boolean abaixo = estoqueService.estaAbaixoDaMinima(id);
//         return ResponseEntity.ok(Map.of(
//             "id", id,
//             "quantidade_atual", estoque.getQuantidade(),
//             "quantidade_minima", estoque.getQuantidadeMinima(),
//             "abaixo_minima", abaixo
//         ));
//     }
}
