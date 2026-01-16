package com.pessoal.dscatalog.servicos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.dto.CategoriaDTO;
import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.dto.projections.ProdutoProjection;
import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.entidades.Produto;
import com.pessoal.dscatalog.infra.excecoes.DatabaseException;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.CategoriaRepository;
import com.pessoal.dscatalog.repositorios.ProdutoRepository;
import com.pessoal.dscatalog.util.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;

	@Transactional(readOnly = true)
	public Page<ProdutoDTO> produtosPaginada(String nome, String categorias, Pageable pageable) {
		List<Long> categoriasIds = new ArrayList<>();
		if (!categorias.equals("0")) {
			categoriasIds = Arrays.asList(categorias.split(",")).stream().map(cat -> Long.parseLong(cat)).toList();
		}
		Page<ProdutoProjection> page = repository.searchProdutos(categoriasIds, nome, pageable);
		List<Long> produtosIds = page.stream().map(produto -> produto.getId()).toList();
		List<Produto> produtos = repository.searchProdutosComCategorias(produtosIds);
		produtos = Utils.ordenarProdutosNaMesma(page.getContent(), produtos);
		List<ProdutoDTO> dtos = produtos.stream().map(produto -> new ProdutoDTO(produto,produto.getCategorias())).toList();
		Page<ProdutoDTO> pageDTO = new PageImpl<>(dtos,page.getPageable(), page.getTotalElements());
		return pageDTO; 
	}

	@Transactional(readOnly = true)
	public ProdutoDTO produtoPorId(Long id) {
		Produto produto = repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("produto não encontrada"));
		return new ProdutoDTO(produto, produto.getCategorias());
	}

	@Transactional
	public ProdutoDTO inserir(ProdutoDTO dto) {
		Produto produto = new Produto();
		dtoToentity(dto, produto);
		produto = repository.save(produto);
		return new ProdutoDTO(produto);
	}

	@Transactional
	public ProdutoDTO atualizar(Long id, ProdutoDTO dto) {
		try {
			Produto produto = repository.getReferenceById(id);
			dtoToentity(dto, produto);
			produto = repository.save(produto);
			return new ProdutoDTO(produto);
		} catch (EntityNotFoundException e) {
			throw new RecursoNaoEncontradoException("Id não encontrado " + id);
		}

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void apagar(Long id) {
		if (!repository.existsById(id)) {
			throw new RecursoNaoEncontradoException("Recurso não encontrado");
		}
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Falha de integridade referencial");
		}
	}
	
	private void dtoToentity(ProdutoDTO dto, Produto produto) {
		produto.setNome(dto.getNome());
		produto.setDescricao(dto.getDescricao());
		produto.setPreco(dto.getPreco());
		produto.setImgUrl(dto.getImgUrl());
		produto.setDate(dto.getDate());
		produto.getCategorias().clear();
		for (CategoriaDTO catDTO : dto.getCategorias()) {
			Categoria categoria =  categoriaRepository.getReferenceById(catDTO.getId());
			produto.getCategorias().add(categoria);
		}
	}
}
