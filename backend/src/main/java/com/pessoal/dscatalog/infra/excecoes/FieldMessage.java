package com.pessoal.dscatalog.infra.excecoes;

public class FieldMessage {
	
	private String campo;
	private String mensagem;
	
	public FieldMessage() {
	}
	
	public FieldMessage(String campo, String mensagem) {
		this.campo = campo;
		this.mensagem = mensagem;
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}
}
