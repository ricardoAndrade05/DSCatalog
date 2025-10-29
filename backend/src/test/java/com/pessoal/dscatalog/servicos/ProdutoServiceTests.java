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
import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.entidades.Produto;
import com.pessoal.dscatalog.infra.Factory;
import com.pessoal.dscatalog.infra.excecoes.DatabaseException;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.CategoriaRepository;
import com.pessoal.dscatalog.repositorios.ProdutoRepository;

@ExtendWith(SpringExtension.class)
public class ProdutoServiceTests {

	private static Long ID_EXISTENTE;
	private static Long ID_INEXISTENTE;
	private static Long ID_COM_DEPENDENCIA;
	private static PageImpl<Produto> page;
	private static Produto produto;
	private static Categoria categoria;
	private static ProdutoDTO produtoDTO;

	@InjectMocks
	private ProdutoService service;

	@Mock
	private ProdutoRepository repository;
	
	@Mock
	private CategoriaRepository categoriaRepository;

	@BeforeEach()
	void setUp() throws Exception {
		ID_EXISTENTE = 1L;
		ID_INEXISTENTE = 1000L;
		ID_COM_DEPENDENCIA = 4L;
		produto = Factory.criarProduto();
		categoria = Factory.criarCategoria();
		produtoDTO = Factory.criarProdutoDTO();
		page = new PageImpl<>(List.of(produto));

		Mockito.when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
		Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(produto);
		Mockito.when(repository.findById(ID_EXISTENTE)).thenReturn(Optional.of(produto));
		Mockito.when(repository.findById(ID_INEXISTENTE)).thenReturn(Optional.empty());
		Mockito.doNothing().when(repository).deleteById(ID_EXISTENTE);
		Mockito.when(repository.existsById(ID_EXISTENTE)).thenReturn(true);
		Mockito.when(repository.existsById(ID_INEXISTENTE)).thenReturn(false);
		Mockito.when(repository.existsById(ID_COM_DEPENDENCIA)).thenReturn(true);
		Mockito.doThrow(RecursoNaoEncontradoException.class).when(repository).deleteById(ID_INEXISTENTE);
		Mockito.doThrow(DatabaseException.class).when(repository).deleteById(ID_COM_DEPENDENCIA);

		Mockito.when(repository.getReferenceById(ID_EXISTENTE)).thenReturn(produto);
		Mockito.doThrow(RecursoNaoEncontradoException.class).when(repository).getReferenceById(ID_INEXISTENTE);
		
		Mockito.when(categoriaRepository.getReferenceById(ID_EXISTENTE)).thenReturn(categoria);
		Mockito.when(categoriaRepository.getReferenceById(ID_INEXISTENTE)).thenThrow(RecursoNaoEncontradoException.class);

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

	@Test
	public void produtoPorIdDeveRetornarProdutoDTOQuandoIdExistir() {
		ProdutoDTO dto = service.produtoPorId(ID_EXISTENTE);
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).findById(ID_EXISTENTE);
	}

	@Test
	public void produtoPorIdDeveLancarRecursoNaoEncontradoExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
			service.produtoPorId(ID_INEXISTENTE);
		});
		Mockito.verify(repository, Mockito.times(1)).findById(ID_INEXISTENTE);
	}

	@Test
	public void atualizarDeveRetornarProdutoDTOQuandoIdExistir() {
		ProdutoDTO dto = service.atualizar(ID_EXISTENTE, produtoDTO);
		Assertions.assertNotNull(dto);
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(ID_EXISTENTE);
		Mockito.verify(repository, Mockito.times(1)).save(ArgumentMatchers.any());
	}

	@Test
	public void atualizarDeveLancarRecursoNaoEncontradoExceptionQuandoIdNaoExistir() {
		Assertions.assertThrows(RecursoNaoEncontradoException.class, () -> {
			service.atualizar(ID_INEXISTENTE, produtoDTO);
		});
		Mockito.verify(repository, Mockito.times(1)).getReferenceById(ID_INEXISTENTE);
	}
}
