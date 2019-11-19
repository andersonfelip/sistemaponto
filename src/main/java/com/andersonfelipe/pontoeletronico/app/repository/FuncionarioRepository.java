package com.andersonfelipe.pontoeletronico.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andersonfelipe.pontoeletronico.app.domain.Funcionario;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

	Funcionario findByPis(String pis);

}
