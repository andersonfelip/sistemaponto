package com.andersonfelipe.pontoeletronico.app.service.impl;

import java.util.Calendar;
import java.util.List;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.andersonfelipe.pontoeletronico.app.constants.Constants;
import com.andersonfelipe.pontoeletronico.app.constants.ModelMapperComponent;
import com.andersonfelipe.pontoeletronico.app.domain.Ponto;
import com.andersonfelipe.pontoeletronico.app.dto.DataFiltroDTO;
import com.andersonfelipe.pontoeletronico.app.dto.PontoDTO;
import com.andersonfelipe.pontoeletronico.app.exceptions.ExceptionBadRequest;
import com.andersonfelipe.pontoeletronico.app.repository.PontoRepository;
import com.andersonfelipe.pontoeletronico.app.service.BancoHorasService;
import com.andersonfelipe.pontoeletronico.app.service.PontoService;
import com.andersonfelipe.pontoeletronico.app.util.Converters;

@Service
@Transactional
public class PontoServiceImpl implements PontoService {

	@Autowired
	private PontoRepository pontoRepository;
	
	@Autowired
	private BancoHorasService bancoHorasService;

	/**
	 * Listar as batidas pelo pis do funcionario agrupados por mês ou por dia.
	 */
	@Override
	public List<PontoDTO> listarBatidasPorFuncionarioDataHoraBatida(String pisFuncionario,String dataBatida) {
		List<Ponto> pontos = null;
		if(StringUtils.isEmpty(dataBatida)) {
			pontos = pontoRepository.findByFuncionarioPis(pisFuncionario);
		}else {
			DataFiltroDTO dataFiltro = Converters.convertFromStringMesOuDiaEmCalendar(dataBatida);
			Calendar dataHoraInicio = dataFiltro.getDataInicio();
			Calendar dataHoraFim = dataFiltro.getDataFim();
			pontos = pontoRepository.findByFuncionarioPisAndDataHoraBatidaBetween(pisFuncionario,dataHoraInicio,dataHoraFim);
		}
		 
		List<PontoDTO> pontosDTO = ModelMapperComponent.modelMapper.map(pontos, new TypeToken<List<PontoDTO>>() {}.getType());
		ModelMapperComponent.modelMapper.validate();
		return pontosDTO;
	}

	/**
	 * Método de incluir nova batida, verifica os campos mandatórios e preenche o tipo de registro (entrada/saida) dependendo do útimo registro.
	 */
	@Override
	public Ponto save(Ponto ponto) {
		
		checarCamposMandatorios(ponto);
		
		Ponto ultimoPonto = getUltimoPonto(ponto);
		
		validar(ponto,ultimoPonto);
		
		preencherTipoRegistro(ponto,ultimoPonto);
		
		bancoHorasService.gerarHorasTrabalhadas(ponto);
		
		bancoHorasService.gerarHorasIntervalo(ponto);
		
		bancoHorasService.gerarHorasNecessariasDescanco(ponto);
		
		return pontoRepository.save(ponto);
	}
	
	/**
	 * Recupera ultimo ponto para o dia da batida
	 * @param ponto
	 * @return
	 */
	private Ponto getUltimoPonto(Ponto ponto) {
		Calendar dataHoraInicio = Constants.getMinHoraDia(ponto.getDataHoraBatida());
		Calendar dataHoraFim = Constants.getMaxHoraDia(ponto.getDataHoraBatida());
		return pontoRepository.findFirstByDataHoraBatidaBetweenOrderByDataHoraBatidaDesc(dataHoraInicio, dataHoraFim);
	}

	/**
	 * Preenche o tipo do registro atual baseado no último registro seguindo a regra:
	 * caso último registro seja do tipo entrada, então o atual é saída.
	 * caso último registro seja do tipo saída, então o atual é entrada.
	 * @param ponto
	 * @param ultimoPonto
	 */
	private void preencherTipoRegistro(Ponto ponto,Ponto ultimoPonto) {
		if(ultimoPonto != null && ultimoPonto.getTipoRegistro().equals(Constants.TIPO_REGISTRO_ENTRADA)) {
			ponto.setTipoRegistro(Constants.TIPO_REGISTRO_SAIDA);
		}else if(ultimoPonto != null && ultimoPonto.getTipoRegistro().equals(Constants.TIPO_REGISTRO_SAIDA)) {
			ponto.setTipoRegistro(Constants.TIPO_REGISTRO_ENTRADA);
		}else {
			ponto.setTipoRegistro(Constants.TIPO_REGISTRO_ENTRADA);
		}
	}
	/**
	 * Checa os campos mandatórios para o parâmetro ponto
	 * @param ponto
	 */
	private void checarCamposMandatorios(Ponto ponto) {

		if(ponto.getDataHoraBatida() == null) {
			throw new ExceptionBadRequest("Data informada é inválida.");
		}
		if(ponto.getFuncionario() == null || StringUtils.isEmpty(ponto.getFuncionario().getPis())) {
			throw new ExceptionBadRequest("Necessário informar o Pis do funcionário.");
		}
	}
	
	/**
	 * Valida algumas regras necessárias.
	 * @param ponto
	 * @param ultimoPonto
	 */
	private void validar(Ponto ponto,Ponto ultimoPonto) {
		//Validar somente uma batida por minuto
		if(ultimoPonto != null && ultimoPonto.getDataHoraBatida() != null && ponto.getDataHoraBatida().equals(ultimoPonto.getDataHoraBatida())) {
			throw new ExceptionBadRequest("Somente é permitido uma batida por minuto no sistema.");
		}
		//Validar se a batida é anterior que a batida passada
		if(ultimoPonto != null && ultimoPonto.getDataHoraBatida() != null && ponto.getDataHoraBatida().before(ultimoPonto.getDataHoraBatida())) {
			throw new ExceptionBadRequest("Não é possível efetuar uma batida antes da batida anterior.");
		}
		
		//Validar se a batida é futura
		if( Calendar.getInstance().before(ponto.getDataHoraBatida())) {
			throw new ExceptionBadRequest("Não é possível efetuar uma batida futura.");
		}
	}

	/**
	 * Recupera o ponto para o Tipo de registro(Entrada/Saída), pis e data e hora do ponto informado.
	 */
	@Override
	public Ponto findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(
			String tipoRegistro, String pis, Calendar dataHoraBatida) {
		return pontoRepository.findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(tipoRegistro, pis, dataHoraBatida);
	}
}
