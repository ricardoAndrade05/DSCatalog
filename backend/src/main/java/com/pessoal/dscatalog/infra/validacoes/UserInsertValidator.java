package com.pessoal.dscatalog.infra.validacoes;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.pessoal.dscatalog.dto.UserInsertDTO;
import com.pessoal.dscatalog.entidades.User;
import com.pessoal.dscatalog.infra.excecoes.FieldMessage;
import com.pessoal.dscatalog.repositorios.UserRepository;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public void initialize(UserInsertValid ann) {
	}

	@Override
	public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		User user = repository.findByEmail(dto.getEmail());
		if (user != null) {
			list.add(new FieldMessage("email", "Email já existe"));
		}

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMensagem()).addPropertyNode(e.getCampo())
					.addConstraintViolation();
		}
		return list.isEmpty();
	}
}
