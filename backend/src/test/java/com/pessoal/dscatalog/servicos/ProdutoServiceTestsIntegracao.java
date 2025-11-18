package com.pessoal.dscatalog.servicos;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.ProdutoRepository;

@SpringBootTest
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
	
}
