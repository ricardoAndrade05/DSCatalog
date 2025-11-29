package com.pessoal.dscatalog.infra.handlers;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.pessoal.dscatalog.infra.excecoes.DatabaseException;
import com.pessoal.dscatalog.infra.excecoes.ErroPadrao;
import com.pessoal.dscatalog.infra.excecoes.ErroValidacao;
import com.pessoal.dscatalog.infra.excecoes.RecursoNaoEncontradoException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {
	
	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ErroPadrao> recursoNaoEncontrado(RecursoNaoEncontradoException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ErroPadrao err = new ErroPadrao();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Recurso não encontrado");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<ErroPadrao> database(DatabaseException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ErroPadrao err = new ErroPadrao();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Database exception ");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		return ResponseEntity.status(status).body(err);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroValidacao> methodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest request) {
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		ErroValidacao err = new ErroValidacao();
		err.setTimestamp(Instant.now());
		err.setStatus(status.value());
		err.setError("Validation exception ");
		err.setMessage(e.getMessage());
		err.setPath(request.getRequestURI());
		for (FieldError f : e.getBindingResult().getFieldErrors()) {
			err.addErro(f.getField(), f.getDefaultMessage());
		}
		return ResponseEntity.status(status).body(err);
	}

}
