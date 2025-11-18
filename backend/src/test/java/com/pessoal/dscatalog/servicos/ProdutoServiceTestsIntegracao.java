package com.pessoal.dscatalog.servicos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.ProdutoRepository;

@SpringBootTest
@Transactional
public class ProdutoServiceTestsIntegracao {

	@Autowired
	private ProdutoService service;
	
	@Autowired
	private ProdutoRepository repository;
	
	private static Long ID_EXISTENTE;
	private static Long ID_INEXISTENTE;
	private static Long TOTAL_DE_PRODUTOS;
	
	@BeforeEach()
	void setUp() throws Exception {
		ID_EXISTENTE = 1L;
		ID_INEXISTENTE = 1000L;
		TOTAL_DE_PRODUTOS = 25L;
	}
	
	@Test
	public void apagarDeveriaDeletarQuandoIdExistir() {
		service.apagar(ID_EXISTENTE);
		
		Assertions.assertEquals(TOTAL_DE_PRODUTOS - 1, repository.count());
	}
	
	@Test
	public void apagarDeveriaLancarExcecaoQuandoIdNaoExistir() {
		Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
			service.apagar(ID_INEXISTENTE);
		});
	}
	
	@Test
	public void produtosDeveriaRetornarTodosProdutosPaginados() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProdutoDTO> page = service.produtos(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10, page.getSize());
		Assertions.assertEquals(TOTAL_DE_PRODUTOS, page.getTotalElements());
	}
	
	@Test
	public void produtosDeveriaRetornarPaginaVaziaQuandoPaginaNaoExistir() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ProdutoDTO> page = service.produtos(pageRequest);
		
		Assertions.assertTrue(page.isEmpty());
	}
	
	@Test
	public void produtosDeveriaRetornarProdutosOrdenadosPorNome() {
		PageRequest pageRequest = PageRequest.of(0, 10,Sort.by("nome"));
		Page<ProdutoDTO> page = service.produtos(pageRequest);
		
		Assertions.assertFalse(page.isEmpty());
		Assertions.assertEquals("Macbook Pro", page.getContent().get(0).getNome());
		Assertions.assertEquals("PC Gamer", page.getContent().get(1).getNome());
		Assertions.assertEquals("PC Gamer Alfa", page.getContent().get(2).getNome());
	}	
}
