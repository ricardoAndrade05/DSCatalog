package com.pessoal.dscatalog.servicos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pessoal.dscatalog.dto.RoleDTO;
import com.pessoal.dscatalog.dto.UserDTO;
import com.pessoal.dscatalog.dto.UserInsertDTO;
import com.pessoal.dscatalog.dto.UserUpdateDTO;
import com.pessoal.dscatalog.entidades.Role;
import com.pessoal.dscatalog.entidades.User;
import com.pessoal.dscatalog.infra.excecoes.DatabaseException;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;
import com.pessoal.dscatalog.repositorios.RoleRepository;
import com.pessoal.dscatalog.repositorios.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Transactional(readOnly = true)
	public Page<UserDTO> users(Pageable pageable) {
		Page<User> page = repository.findAll(pageable);
		return page.map(user -> new UserDTO(user));
	}
	
	@Transactional(readOnly = true)
	public UserDTO userPorId(Long id) {
		User user = repository.findById(id)
				.orElseThrow(() -> new RecursoNaoEncontradoException("produto não encontrada"));
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO inserir(UserInsertDTO dto) {
		User user = new User();
		dtoToentity(dto, user);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user = repository.save(user);
		return new UserDTO(user);
	}

	@Transactional
	public UserDTO atualizar(Long id, UserUpdateDTO dto) {
		try {
			User user = repository.getReferenceById(id);
			dtoToentity(dto, user);
			user = repository.save(user);
			return new UserDTO(user);
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
	
	private void dtoToentity(UserDTO dto, User user) {
		user.setFirstName(dto.getFirstName());
		user.setLastName(dto.getLastName());
		user.setEmail(dto.getEmail());	
		user.getRoles().clear();
		for(RoleDTO roleDTO : dto.getRoles()) {
			Role role = roleRepository.getReferenceById(roleDTO.getId());
			user.getRoles().add(role);
		}
	}
}
