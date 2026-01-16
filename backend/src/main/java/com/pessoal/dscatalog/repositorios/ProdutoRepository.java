package com.pessoal.dscatalog.repositorios;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pessoal.dscatalog.dto.projections.ProdutoProjection;
import com.pessoal.dscatalog.entidades.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query(nativeQuery = true, value = 
		    "SELECT DISTINCT p.id, p.nome " +
		    "FROM tb_produto p " +
		    "JOIN tb_produto_categoria pc ON p.id = pc.produto_id " +
		    "WHERE (:categoriasIds IS NULL OR pc.categoria_id IN :categoriasIds) " +
		    "AND LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) " +
		    "ORDER BY p.nome",
		    
		    countQuery = 
		    "SELECT COUNT(DISTINCT p.id) " +
		    "FROM tb_produto p " +
		    "JOIN tb_produto_categoria pc ON p.id = pc.produto_id " +
		    "WHERE (:categoriasIds IS NULL OR pc.categoria_id IN :categoriasIds) " +
		    "AND LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
	Page<ProdutoProjection> searchProdutos(List<Long>categoriasIds,String nome ,Pageable pageable);
	
	@Query("SELECT p FROM Produto p JOIN FETCH p.categorias WHERE p.id IN :ids ORDER BY p.nome")
	List<Produto> searchProdutosComCategorias(List<Long> ids);
}
