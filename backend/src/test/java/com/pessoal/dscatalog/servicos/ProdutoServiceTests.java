package com.pessoal.dscatalog.servicos;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.entidades.Produto;
import com.pessoal.dscatalog.infra.Factory;
import com.pessoal.dscatalog.infra.excecoes.DatabaseException;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.ProdutoRepository;

@ExtendWith(SpringExtension.class)
public class ProdutoServiceTests {

	private static Long ID_EXISTENTE;
	private static Long ID_INEXISTENTE;
	private static Long ID_COM_DEPENDENCIA;
	private static PageImpl<Produto> page;
	private static Produto produto;
	

	@InjectMocks
	private ProdutoService service;

	@Mock
	private ProdutoRepository repository;

	@BeforeEach()
	void setUp() throws Exception {
		ID_EXISTENTE = 1L;
		ID_INEXISTENTE = 1000L;
		ID_COM_DEPENDENCIA = 4L;
		produto = Factory.criarProduto();
		page = new PageImpl<>(List.of(produto));
		
		Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(produto);
		Mockito.when(repository.findById(ID_EXISTENTE)).thenReturn(Optional.of(produto));
		Mockito.when(repository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());
		Mockito.doNothing().when(repository).deleteById(ID_EXISTENTE);
		Mockito.when(repository.existsById(ID_EXISTENTE)).thenReturn(true);
		Mockito.when(repository.existsById(ID_INEXISTENTE)).thenReturn(false);
		Mockito.when(repository.existsById(ID_COM_DEPENDENCIA)).thenReturn(true);
		Mockito.doThrow(RecursoNaoEncontradoException.class).when(repository).deleteById(ID_INEXISTENTE);
		Mockito.doThrow(DatabaseException.class).when(repository).deleteById(ID_COM_DEPENDENCIA);
	}

	@Test
	public void apagarNaoDeveLancarExcecaoQuandoIdExistir() {
		Assertions.assertDoesNotThrow(() -> {
			service.apagar(ID_EXISTENTE);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(ID_EXISTENTE);
	}

	@Test
	public void apagarDeveLancarRecursoNaoEncontradoExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
			service.apagar(ID_INEXISTENTE);
		});
		Mockito.verify(repository, Mockito.never()).deleteById(ID_INEXISTENTE);
	}

	@Test
	public void apagarDeveLancarDatabaseExceptionQuandoIdTiverDependencia() {
		Assertions.assertThrows(DatabaseException.class, () -> {
			service.apagar(ID_COM_DEPENDENCIA);
		});
		Mockito.verify(repository, Mockito.times(1)).deleteById(ID_COM_DEPENDENCIA);
	}
	
	@Test
	public void produtosDeveRetornarPaginaDeProdutos() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProdutoDTO> pageProdutos = service.produtos(pageable);
		Assertions.assertNotNull(pageProdutos);
		Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
	}
}
