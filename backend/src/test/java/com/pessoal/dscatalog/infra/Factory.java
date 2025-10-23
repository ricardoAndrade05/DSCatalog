package com.pessoal.dscatalog.infra;

import java.time.Instant;

import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.entidades.Produto;

public class Factory {

	
	public static Produto criarProduto() {
		Produto produto = new Produto(1L, "Computador", "Bom computador", 1200.0,Instant.parse("2020-10-20T03:00:00Z"), "https://img.com/img.png");
		produto.getCategorias().add(new Categoria(2L, "Eletrônicos"));
		return produto;
	}
	
	public static ProdutoDTO criarProdutoDTO() {
		Produto produto = criarProduto();
		return new ProdutoDTO(produto, produto.getCategorias());
	}
}
