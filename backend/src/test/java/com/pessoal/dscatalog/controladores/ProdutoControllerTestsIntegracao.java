package com.pessoal.dscatalog.controladores;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProdutoControllerTestsIntegracao {
	
	
	private Long idExistente;
	private Long idInexistente;
	private Long contagemTotalDeProdutos;
	
	@Autowired
	private MockMvc mockMvc;
	
	@BeforeEach()
	void setUp() throws Exception {
		idExistente = 1L;
		idInexistente = 1000L;
		contagemTotalDeProdutos = 25L;
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
}
