package com.pessoal.dscatalog.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.dto.CategoriaDTO;
import com.pessoal.dscatalog.entidades.Categoria;
import com.pessoal.dscatalog.infra.excecoes.DatabaseException;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.CategoriaRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	@Transactional(readOnly = true)
	public Page<CategoriaDTO> categorias(Pageable pageable) {
		Page<Categoria> page = repository.findAll(pageable);
		return page.map(categoria -> new CategoriaDTO(categoria));
	}

	@Transactional(readOnly = true)
	public CategoriaDTO categoriaPorId(Long id) {
		Categoria categoria = repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("Categoria não encontrada"));
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

}
