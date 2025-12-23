package com.pessoal.dscatalog.controladores;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.infra.Factory;
import com.pessoal.dscatalog.infra.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProdutoControllerTestsIntegracao {
	
	
	private Long idExistente;
	private Long idInexistente;
	private Long contagemTotalDeProdutos;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private TokenUtil tokenUtil;
	
	private String username, password, bearerToken;
	
	@BeforeEach()
	void setUp() throws Exception {
		idExistente = 1L;
		idInexistente = 1000L;
		contagemTotalDeProdutos = 25L;
		
		username = "maria@gmail.com";
		password = "123456";
		
		bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);
	}

	@Test
	public void produtosDeveriaRetornarPaginacaoQuandoSortPorNome() throws Exception {
		ResultActions result = 
				mockMvc.perform(get("/produtos?sort=nome,asc")
				.accept(MediaType.APPLICATION_JSON));
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(jsonPath("$.totalElements").value(contagemTotalDeProdutos));
		result.andExpect(jsonPath("$.content[0].nome").value("Macbook Pro"));
		result.andExpect(jsonPath("$.content[1].nome").value("PC Gamer"));
		result.andExpect(jsonPath("$.content[2].nome").value("PC Gamer Alfa"));
		
	}
	
	@Test
	public void atualizarDeveriaRetornarNaoEncontradoQuandoIdNaoExiste() throws Exception {
		ProdutoDTO produtoDTO = Factory.criarProdutoDTO();
		String corpoJson = objectMapper.writeValueAsString(produtoDTO);
				
		ResultActions result = mockMvc.perform(put("/produtos/{id}", idInexistente)
				.header("Authorization", "Bearer " + bearerToken)
				.content(corpoJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)); 
		
		result.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	public void atualizarDeveriaRetornarProdutoDTOQuandoIdExiste() throws Exception {
		ProdutoDTO produtoDTO = Factory.criarProdutoDTO();
		String corpoJson = objectMapper.writeValueAsString(produtoDTO);
		
		String nomeEsperado = produtoDTO.getNome();
		String descricaoEsperada = produtoDTO.getDescricao();
		Long idEsperado = produtoDTO.getId();

		
		ResultActions result = mockMvc.perform(put("/produtos/{id}", idExistente)
				.header("Authorization", "Bearer " + bearerToken)
				.content(corpoJson)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)); 
		
		result.andExpect(MockMvcResultMatchers.status().isOk());
		result.andExpect(jsonPath("$.id").value(idEsperado));
		result.andExpect(jsonPath("$.nome").value(nomeEsperado));
		result.andExpect(jsonPath("$.descricao").value(descricaoEsperada));
	}
	
	
}
