// package com.meuecommerce.estoque.services;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertFalse;
// import static org.junit.jupiter.api.Assertions.assertNotNull;
// import static org.junit.jupiter.api.Assertions.assertThrows;
// import static org.junit.jupiter.api.Assertions.assertTrue;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.ActiveProfiles;

// import com.meuecommerce.estoque.domain.Estoque;
// import com.meuecommerce.estoque.exceptions.EstoqueNaoEncontradoException;
// import com.meuecommerce.estoque.exceptions.QuantidadeInsuficienteException;
// import com.meuecommerce.estoque.repositories.EstoqueRepository;

// @SpringBootTest
// @ActiveProfiles("test")
// public class EstoqueServiceTest {

//     @Autowired
//     private EstoqueService estoqueService;

//     @Autowired
//     private EstoqueRepository estoqueRepository;

//     @BeforeEach
//     public void setup() {
//         estoqueRepository.deleteAll();
//     }

//     @Test
//     public void testCriarEstoque() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         assertNotNull(estoque.getId());
//         assertEquals("SKU001", estoque.getSku());
//         assertEquals("Produto Teste", estoque.getDescricao());
//         assertEquals(100, estoque.getQuantidade());
//         assertEquals(10, estoque.getQuantidadeMinima());
//     }

//     @Test
//     public void testAdicionarQuantidade() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         Estoque atualizado = estoqueService.adicionarQuantidade(estoque.getId(), 50);

//         assertEquals(150, atualizado.getQuantidade());
//     }

//     @Test
//     public void testRemoverQuantidade() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         Estoque atualizado = estoqueService.removerQuantidade(estoque.getId(), 30);

//         assertEquals(70, atualizado.getQuantidade());
//     }

//     @Test
//     public void testRemoverQuantidadeInsuficiente() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         assertThrows(QuantidadeInsuficienteException.class, () -> {
//             estoqueService.removerQuantidade(estoque.getId(), 150);
//         });
//     }

//     @Test
//     public void testBuscarEstoquePorId() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         Estoque encontrado = estoqueService.buscarEstoquePorId(estoque.getId());

//         assertEquals(estoque.getId(), encontrado.getId());
//         assertEquals("SKU001", encontrado.getSku());
//     }

//     @Test
//     public void testBuscarEstoquePorIdNaoEncontrado() {
//         assertThrows(EstoqueNaoEncontradoException.class, () -> {
//             estoqueService.buscarEstoquePorId(999L);
//         });
//     }

//     @Test
//     public void testBuscarEstoquePorSku() {
//         estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         Estoque encontrado = estoqueService.buscarEstoquePorSku("SKU001");

//         assertEquals("SKU001", encontrado.getSku());
//     }

//     @Test
//     public void testVerificarDisponibilidade() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         assertTrue(estoqueService.verificarDisponibilidade(estoque.getId(), 50));
//         assertTrue(estoqueService.verificarDisponibilidade(estoque.getId(), 100));
//         assertFalse(estoqueService.verificarDisponibilidade(estoque.getId(), 150));
//     }

//     @Test
//     public void testEstaAbaixoDaMinima() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         assertFalse(estoqueService.estaAbaixoDaMinima(estoque.getId()));

//         estoqueService.removerQuantidade(estoque.getId(), 95);

//         assertTrue(estoqueService.estaAbaixoDaMinima(estoque.getId()));
//     }

//     @Test
//     public void testDeletarEstoque() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         estoqueService.deletarEstoque(estoque.getId());

//         assertThrows(EstoqueNaoEncontradoException.class, () -> {
//             estoqueService.buscarEstoquePorId(estoque.getId());
//         });
//     }

//     @Test
//     public void testCriarEstoqueComSkuDuplicado() {
//         estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         assertThrows(IllegalArgumentException.class, () -> {
//             estoqueService.criarEstoque(
//                 "SKU001",
//                 "Outro Produto",
//                 50,
//                 5
//             );
//         });
//     }

//     @Test
//     public void testAdicionarQuantidadeNegativa() {
//         Estoque estoque = estoqueService.criarEstoque(
//             "SKU001",
//             "Produto Teste",
//             100,
//             10
//         );

//         assertThrows(IllegalArgumentException.class, () -> {
//             estoqueService.adicionarQuantidade(estoque.getId(), -10);
//         });
//     }
// }
