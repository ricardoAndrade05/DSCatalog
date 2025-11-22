package com.pessoal.dscatalog.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pessoal.dscatalog.entidades.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	

}
