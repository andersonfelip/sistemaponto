package com.andersonfelipe.pontoeletronico.app.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import com.andersonfelipe.pontoeletronico.app.domain.BancoHoras;
import com.andersonfelipe.pontoeletronico.app.domain.Ponto;
import com.andersonfelipe.pontoeletronico.app.dto.DataFiltroDTO;

public interface BancoHorasService {

	public void gerarHorasTrabalhadas(Ponto ponto);

	public BancoHoras findByFuncionarioPisAndDataHoraBancoHoras(String pisFuncionario, Calendar dataBancoHoras);

	public List<BancoHoras> findByFuncionarioPisAndDataHoraBancoHorasBetween(String pisFuncionario, Calendar dataHoraInicio,
			Calendar dataHoraFim);

	public BigDecimal saldoHorasTrabalhadas(String pisFuncionario, DataFiltroDTO dataFiltro);

	public void gerarHorasIntervalo(Ponto ponto);

	public void gerarHorasNecessariasDescanco(Ponto ponto);

	public BigDecimal saldoNecessarioDescanco(String pisFuncionario, DataFiltroDTO dataFiltro);

}
