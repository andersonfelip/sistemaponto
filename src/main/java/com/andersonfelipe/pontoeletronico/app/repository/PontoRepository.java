package com.andersonfelipe.pontoeletronico.app.repository;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.andersonfelipe.pontoeletronico.app.domain.Ponto;

public interface PontoRepository extends JpaRepository<Ponto, Long> {
	List<Ponto> findByFuncionarioPis(String pisFuncionario);
	
	List<Ponto> findByFuncionarioPisAndDataHoraBatidaBetween(String pisFuncionario,Calendar dataHoraInicio, Calendar dataHoraFim);
	
	Ponto findFirstByDataHoraBatidaBetweenOrderByDataHoraBatidaDesc(Calendar dataHoraInicio, Calendar dataHoraFim);
	
	Ponto findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(String tipoRegistro,String pisFuncionario,Calendar dataHoraBatida);
}
