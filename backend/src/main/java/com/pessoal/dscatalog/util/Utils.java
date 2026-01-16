package com.pessoal.dscatalog.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pessoal.dscatalog.dto.projections.ProdutoProjection;
import com.pessoal.dscatalog.entidades.Produto;

public class Utils {

	public static List<Produto> ordenarProdutosNaMesma(List<ProdutoProjection> listaOrdenada, List<Produto> listaDesordenada) {
		Map<Long, Produto> map = new HashMap<>();
		for (Produto produto : listaDesordenada) {
			map.put(produto.getId(), produto);
		}
		List<Produto> listaFinal = new ArrayList<>();
		for (ProdutoProjection pp : listaOrdenada) {
			listaFinal.add(map.get(pp.getId()));
		}
		return listaFinal;
	}	
}