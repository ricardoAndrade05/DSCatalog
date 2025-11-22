package com.pessoal.dscatalog.entidades;

import com.pessoal.dscatalog.dto.UserDTO;

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