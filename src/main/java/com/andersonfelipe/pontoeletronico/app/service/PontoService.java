package com.andersonfelipe.pontoeletronico.app.service;

import java.util.Calendar;
import java.util.List;

import com.andersonfelipe.pontoeletronico.app.domain.Ponto;
import com.andersonfelipe.pontoeletronico.app.dto.PontoDTO;

public interface PontoService {
	public Ponto save(Ponto ponto);

	public List<PontoDTO> listarBatidasPorFuncionarioDataHoraBatida(String pisFuncionario, String dataBatida);

	public Ponto findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(
			String tipoRegistroEntrada, String pis, Calendar dataHoraBatida);
}
