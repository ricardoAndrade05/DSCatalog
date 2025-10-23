package com.pessoal.dscatalog.repositorios;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.pessoal.dscatalog.entidades.Produto;
import com.pessoal.dscatalog.infra.Factory;

@DataJpaTest
public class ProdutoRepositoryTests {

	@Autowired
	private ProdutoRepository repository;
	
	private long idExistente;
	private long idInexistente;
	private long totalDeProdutos;
	
	@BeforeEach
	void setup() {
		idExistente = 1L;
		idInexistente = 1000L;
		totalDeProdutos = 25L;
	}
	
	@Test
	public void salvarDevePersistirAutoIncrementoQuandoIdForNull() {
		Produto produto = Factory.criarProduto();
		produto.setId(null);
		
		produto = repository.save(produto);
		
		Assertions.assertNotNull(produto.getId());
		Assertions.assertEquals(totalDeProdutos + 1L, produto.getId());
	}
	
	@Test
	public void apagarDeveRemoverProdutoQuandoExistir() {
		repository.deleteById(idExistente);
		
		Optional<Produto> resultado = repository.findById(idExistente);
		
		Assertions.assertFalse(resultado.isPresent());
	}
	
	@Test
	public void buscarPorIdDeveRetornarProdutoQuandoExistir() {
		Optional<Produto> resultado = repository.findById(idExistente);
		
		Assertions.assertTrue(resultado.isPresent());
	}
	
	@Test
	public void buscarPorIdDeveRetornarVazioQuandoNaoExistir() {
		Optional<Produto> resultado = repository.findById(idInexistente);
		
		Assertions.assertFalse(resultado.isPresent());
	}
}
