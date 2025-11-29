package com.pessoal.dscatalog.dto;

import com.pessoal.dscatalog.infra.validacoes.UserInsertValid;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
	private String password;

	public UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}