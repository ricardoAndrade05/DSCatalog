package com.pessoal.dscatalog.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.entidades.Produto;

public class ProdutoDTO {

	private Long id;
	private String nome;
	private String descricao;
	private Double preco;
	private String imgUrl;
	private Instant date;

	private List<CategoriaDTO> categorias = new ArrayList<>();

	public ProdutoDTO() {

	}

	public ProdutoDTO(Long id, String nome, String descricao, Double preco, String imgUrl, Instant date) {
		super();
		this.id = id;
		this.nome = nome;
		this.descricao = descricao;
		this.preco = preco;
		this.imgUrl = imgUrl;
		this.date = date;
	}

	public ProdutoDTO(Produto produto) {
		this.id = produto.getId();
		this.nome = produto.getNome();
		this.descricao = produto.getDescricao();
		this.preco = produto.getPreco();
		this.imgUrl = produto.getImgUrl();
		this.date = produto.getDate();
	}

	public ProdutoDTO(Produto produto, Set<Categoria> categorias) {
		this(produto);
		categorias.forEach(cat -> this.categorias.add(new CategoriaDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public Double getPreco() {
		return preco;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public List<CategoriaDTO> getCategorias() {
		return categorias;
	}

	public Instant getDate() {
		return date;
	}

}
