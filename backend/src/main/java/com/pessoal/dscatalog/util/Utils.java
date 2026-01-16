package com.pessoal.dscatalog.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pessoal.dscatalog.dto.projections.IdProjection;

public class Utils {

	public static <ID> List< ? extends IdProjection<ID>> ordenarProdutosNaMesma(List<? extends IdProjection<ID>> listaOrdenada, List<? extends IdProjection<ID>> listaDesordenada) {
		Map<ID,IdProjection<ID>> map = new HashMap<>();
		for (IdProjection<ID> obj : listaDesordenada) {
			map.put(obj.getId(), obj);
		}
		List<IdProjection<ID>> listaFinal = new ArrayList<>();
		for (IdProjection<ID> pp : listaOrdenada) {
			listaFinal.add(map.get(pp.getId()));
		}
		return listaFinal;
	}	
}