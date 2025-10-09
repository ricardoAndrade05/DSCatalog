package com.pessoal.dscatalog.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pessoal.dscatalog.entidades.Categoria;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaController {

	@GetMapping
	public ResponseEntity<List<Categoria>> findAll() {
		List<Categoria> list = List.of(new Categoria(1L, "Eletrônicos"), new Categoria(2L, "Livros"));
		return ResponseEntity.ok().body(list);
	}

}
