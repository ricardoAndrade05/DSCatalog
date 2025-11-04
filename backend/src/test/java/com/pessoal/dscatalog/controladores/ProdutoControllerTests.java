package com.pessoal.dscatalog.controladores;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.infra.Factory;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.servicos.ProdutoService;

@WebMvcTest(ProdutoController.class)
public class ProdutoControllerTests {
	
	private PageImpl<ProdutoDTO> page;
	private ProdutoDTO produtoDTO;
	private Long idExistente;
	private Long idInexistente;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private ProdutoService service;
	
	@BeforeEach()
	void setUp() throws Exception {
		idExistente = 1L;
		idInexistente = 1000L;
		produtoDTO = Factory.criarProdutoDTO();
		page = new PageImpl<>(List.of(produtoDTO));
		
		when(service.produtos(ArgumentMatchers.any())).thenReturn(page);
		
		when(service.produtoPorId(idExistente)).thenReturn(produtoDTO);
		when(service.produtoPorId(idInexistente)).thenThrow(RecursoNaoEncontradoException.class);
		
		when(service.atualizar(eq(idExistente),any())).thenReturn(produtoDTO);
		when(service.atualizar(eq(idInexistente),any())).thenThrow(RecursoNaoEncontradoException.class);
	}
	
	@Test
	public void listarProdutosDeveriaRetornarPaginaDeProdutos() throws Exception {
		ResultActions result = mockMvc.perform(get("/produtos").accept(MediaType.APPLICATION_JSON)); 
		result.andExpect(status().isOk());
	}
	
	@Test
	public void buscarProdutoPorIdDeveriaRetornarProdutoQuandoIdExistir() throws Exception {
		ResultActions result = mockMvc.perform(get("/produtos/{id}", idExistente).accept(MediaType.APPLICATION_JSON)); 
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.descricao").exists());
	}
	
	@Test
	public void buscarProdutoPorIdDeveriaRetornar404QuandoIdNaoExistir() throws Exception {
		ResultActions result = mockMvc.perform(get("/produtos/{id}", idInexistente).accept(MediaType.APPLICATION_JSON)); 
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void atualizarDeveriaRetornarProdutoQuandoIdExistir() throws Exception {
		String corpoJson = objectMapper.writeValueAsString(produtoDTO);
		
		ResultActions result = mockMvc.perform(put("/produtos/{id}", idExistente)
				.content(corpoJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)); 
		
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.nome").exists());
		result.andExpect(jsonPath("$.descricao").exists());
	}
	
	@Test
	public void atualizarDeveriaRetornar404QuandoIdNaoExistir() throws Exception {
		String corpoJson = objectMapper.writeValueAsString(produtoDTO);
		
		ResultActions result = mockMvc.perform(put("/produtos/{id}", idInexistente)
				.content(corpoJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)); 
		result.andExpect(status().isNotFound());	
	}
}
