package com.andersonfelipe.pontoeletronico.app.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andersonfelipe.pontoeletronico.app.domain.BancoHoras;

public interface BancoHorasRepository  extends JpaRepository<BancoHoras, Long>  {
	BancoHoras findByFuncionarioPisAndDataHoraBancoHoras(String pisFuncionario,Calendar dataBancoHoras);
	
	List<BancoHoras> findByFuncionarioPisAndDataHoraBancoHorasBetween(String pisFuncionario,Calendar dataHoraInicio, Calendar dataHoraFim);
}
