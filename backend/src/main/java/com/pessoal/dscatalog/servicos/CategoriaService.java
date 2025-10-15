package com.pessoal.dscatalog.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.dto.CategoriaDTO;
import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.infra.excecoes.EntidadeNaoEncontradaException;
import com.pessoal.dscatalog.repositorios.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	@Transactional(readOnly = true)
	public List<CategoriaDTO> categorias() {
		List<Categoria> categorias = repository.findAll();
		return categorias.stream().map(categoria -> new CategoriaDTO(categoria)).toList();
	}

	@Transactional(readOnly = true)
	public CategoriaDTO categoriaPorId(Long id) {
		Categoria categoria = repository.findById(id).orElseThrow(() -> new EntidadeNaoEncontradaException("Categoria não encontrada"));
		return new CategoriaDTO(categoria);
	}

	@Transactional
	public CategoriaDTO inserir(CategoriaDTO dto) {
		Categoria categoria = new Categoria();
		categoria.setNome(dto.getNome());
		categoria = repository.save(categoria);	
		return new CategoriaDTO(categoria);
	}

	@Transactional
	public CategoriaDTO atualizar(Long id, CategoriaDTO dto) {
		try {
			Categoria categoria = repository.getReferenceById(id);
			categoria.setNome(dto.getNome());
			categoria = repository.save(categoria);
			return new CategoriaDTO(categoria);
		} catch (EntityNotFoundException e) {
			throw new EntidadeNaoEncontradaException("Id não encontrado " + id);
		}

	}

}
