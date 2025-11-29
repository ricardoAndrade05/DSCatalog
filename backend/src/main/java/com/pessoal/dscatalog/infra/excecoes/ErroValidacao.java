package com.pessoal.dscatalog.infra.excecoes;

import java.util.ArrayList;
import java.util.List;

public class ErroValidacao extends ErroPadrao {
	
	private List<FieldMessage> erros = new ArrayList<>();
	
	public List<FieldMessage> getErros() {
		return erros;
	}

	public void addErro(String campo, String mensagem) {
		this.erros.add(new FieldMessage(campo, mensagem));
	}

}
