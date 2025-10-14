package com.pessoal.dscatalog.controladores;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pessoal.dscatalog.dto.CategoriaDTO;
import com.pessoal.dscatalog.servicos.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

	@Autowired
	private CategoriaService service;
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> categorias() {
		List<CategoriaDTO> list = service.categorias();
		return ResponseEntity.ok().body(list);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<CategoriaDTO> categoriaPorId(@PathVariable Long id) {
		CategoriaDTO dto = service.categoriaPorId(id);
		return ResponseEntity.ok().body(dto);
	}

}
