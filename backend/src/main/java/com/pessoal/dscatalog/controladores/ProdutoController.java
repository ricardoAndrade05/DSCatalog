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

import com.pessoal.dscatalog.dto.ProdutoDTO;
import com.pessoal.dscatalog.servicos.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoController {

	@Autowired
	private ProdutoService service;

	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> Produtos(Pageable pageable) {
		Page<ProdutoDTO> list = service.produtos(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ProdutoDTO> ProdutoPorId(@PathVariable Long id) {
		ProdutoDTO dto = service.produtoPorId(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<ProdutoDTO> inserir(@RequestBody ProdutoDTO dto) {
		dto = service.inserir(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProdutoDTO> atualizar(@PathVariable Long id, @RequestBody ProdutoDTO dto) {
		dto = service.atualizar(id, dto);
		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ProdutoDTO> apagar(@PathVariable Long id) {
		service.apagar(id);
		return ResponseEntity.noContent().build();
	}

}
