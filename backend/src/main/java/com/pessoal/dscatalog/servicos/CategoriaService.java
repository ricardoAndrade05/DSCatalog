package com.pessoal.dscatalog.servicos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.repositorios.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	@Transactional(readOnly = true)
	public List<Categoria> findAll() {
		List<Categoria> categorias = repository.findAll();
		return categorias;
	}

}
