package com.pessoal.dscatalog.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.dto.CategoriaDTO;
import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.infra.excecoes.EntidadeNaoEncontradaException;
import com.pessoal.dscatalog.repositorios.CategoriaRepository;

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

}
