package com.pessoal.dscatalog.controladores;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.pessoal.dscatalog.dto.UserDTO;
import com.pessoal.dscatalog.dto.UserInsertDTO;
import com.pessoal.dscatalog.servicos.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {

	@Autowired
	private UserService service;

	@GetMapping
	public ResponseEntity<Page<UserDTO>> usuarios(Pageable pageable) {
		Page<UserDTO> list = service.users(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<UserDTO> usuarioPorId(@PathVariable Long id) {
		UserDTO dto = service.userPorId(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<UserDTO> inserir(@Valid @RequestBody UserInsertDTO dto) {
		UserDTO userDTO = service.inserir(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(userDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<UserDTO> atualizar(@PathVariable Long id,@Valid @RequestBody UserDTO dto) {
		dto = service.atualizar(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<UserDTO> apagar(@PathVariable Long id) {
		service.apagar(id);
		return ResponseEntity.noContent().build();
	}

}
