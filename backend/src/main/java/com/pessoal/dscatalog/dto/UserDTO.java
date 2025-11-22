package com.pessoal.dscatalog.dto;

import java.util.HashSet;
import java.util.Set;

import com.pessoal.dscatalog.entidades.User;

public class UserDTO {

	private Long id;
	private String firstName;
	private String lastName;
	private String email;
	private Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO() {
		
	}
	
	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public UserDTO(User user) {
		this.id = user.getId();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public Long getId() {
		return id;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}
	
}
