package com.pessoal.dscatalog.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pessoal.dscatalog.entidades.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	

}
