package com.andersonfelipe.pontoeletronico.app.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andersonfelipe.pontoeletronico.app.constants.Constants;
import com.andersonfelipe.pontoeletronico.app.domain.BancoHoras;
import com.andersonfelipe.pontoeletronico.app.domain.Ponto;
import com.andersonfelipe.pontoeletronico.app.dto.DataFiltroDTO;
import com.andersonfelipe.pontoeletronico.app.repository.BancoHorasRepository;
import com.andersonfelipe.pontoeletronico.app.repository.PontoRepository;
import com.andersonfelipe.pontoeletronico.app.service.BancoHorasService;
import com.andersonfelipe.pontoeletronico.app.util.Converters;

@Service
@Transactional
public class BancoHorasServiceImpl implements BancoHorasService{

	@Autowired
	private BancoHorasRepository bancoHorasRepository;
	
	@Autowired
	private PontoRepository  pontoRepository;
	
	@Override
	public BancoHoras findByFuncionarioPisAndDataHoraBancoHoras(String pisFuncionario,Calendar dataBancoHoras) {
		return bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHoras(pisFuncionario, dataBancoHoras);
	}
	
	@Override
	public List<BancoHoras> findByFuncionarioPisAndDataHoraBancoHorasBetween(String pisFuncionario,Calendar dataHoraInicio, Calendar dataHoraFim) {
		return bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHorasBetween(pisFuncionario, dataHoraInicio, dataHoraFim);
	}
	
	@Override
	public BigDecimal saldoHorasTrabalhadas(String pisFuncionario,DataFiltroDTO dataFiltro) {
		BigDecimal saldoHorasTrabalhadas = BigDecimal.ZERO;
		
		List<BancoHoras> bancoHoras = bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHorasBetween(pisFuncionario, dataFiltro.getDataInicio(), dataFiltro.getDataFim());
		
		for (BancoHoras item : bancoHoras) {
			if(item.getSaldoMinutos() != null) {
				saldoHorasTrabalhadas = saldoHorasTrabalhadas.add(item.getSaldoMinutos());	
			}
		}
		return Converters.convertFromMinuteToHour(saldoHorasTrabalhadas);
	}
	
	@Override
	public BigDecimal saldoNecessarioDescanco(String pisFuncionario,DataFiltroDTO dataFiltro) {
		BigDecimal saldoMinutosIntervalo = BigDecimal.ZERO;
		BigDecimal saldoMinutosNecessarioDescanco = BigDecimal.ZERO;
		
		List<BancoHoras> bancoHoras = bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHorasBetween(pisFuncionario, dataFiltro.getDataInicio(), dataFiltro.getDataFim());
		
		for (BancoHoras item : bancoHoras) {
			saldoMinutosIntervalo = saldoMinutosIntervalo.add(item.getSaldoMinutosIntervalo() != null ? item.getSaldoMinutosIntervalo() : BigDecimal.ZERO);
			saldoMinutosNecessarioDescanco = saldoMinutosNecessarioDescanco.add(item.getSaldoNecessarioDescanco() != null ? item.getSaldoNecessarioDescanco() : BigDecimal.ZERO);
		}
		saldoMinutosNecessarioDescanco = saldoMinutosNecessarioDescanco.subtract(saldoMinutosIntervalo);
		if (saldoMinutosNecessarioDescanco.compareTo(BigDecimal.ZERO) > 0)
			return saldoMinutosNecessarioDescanco;
		else
			return BigDecimal.ZERO;
	}
	
	@Override
	public void gerarHorasTrabalhadas(Ponto ponto) {
		if(ponto.getTipoRegistro().equals(Constants.TIPO_REGISTRO_SAIDA)) {
			
			Calendar dataBancoHoras =Constants.getMinHoraDia(ponto.getDataHoraBatida());
			
			Ponto pontoEntrada = pontoRepository.findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(Constants.TIPO_REGISTRO_ENTRADA, ponto.getFuncionario().getPis(), ponto.getDataHoraBatida());
			BigDecimal minutosTrabalhados = contabilizarHorasTrabalhadas(pontoEntrada.getDataHoraBatida(),ponto.getDataHoraBatida());
			
			BancoHoras bancoHoras = bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHoras(ponto.getFuncionario().getPis(), dataBancoHoras);
			
			if(bancoHoras == null) {
				bancoHoras = new BancoHoras();
				bancoHoras.setDataHoraBancoHoras(dataBancoHoras);
				bancoHoras.setFuncionario(ponto.getFuncionario());
				bancoHoras.setSaldoMinutos(minutosTrabalhados);
			}else {
				BigDecimal saldoMinutos = bancoHoras.getSaldoMinutos();
				bancoHoras.setSaldoMinutos(saldoMinutos.add(minutosTrabalhados));
				
			}
			bancoHorasRepository.save(bancoHoras);
		}
	}
	
	private BigDecimal contabilizarHorasTrabalhadas(Calendar dataEntrada,Calendar dataSaida){
		
		int horaEntrada = dataEntrada.get(Calendar.HOUR_OF_DAY);
		int minutoEntrada = dataEntrada.get(Calendar.MINUTE);
		int minutosTotaisEntrada = Converters.convertFromHourToMinute(horaEntrada) + minutoEntrada;

		int horaSaida = dataSaida.get(Calendar.HOUR_OF_DAY);
		int minutoSaida = dataSaida.get(Calendar.MINUTE);
		int minutosTotaisSaida = Converters.convertFromHourToMinute(horaSaida) + minutoSaida;
		
		BigDecimal minutosTrabalhados = new BigDecimal(minutosTotaisSaida -minutosTotaisEntrada);
		
		minutosTrabalhados = acrescentarAdicionalNoturno(minutosTrabalhados, horaSaida,horaEntrada, minutoSaida,minutoEntrada);
		
		if(dataSaida.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			minutosTrabalhados = adicionalDomingo(minutosTrabalhados);
		}else if(dataSaida.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			minutosTrabalhados = adicionalSabado(minutosTrabalhados);
		}
		
		return minutosTrabalhados;
	}
	
	private BigDecimal contabilizarTempoIntervalo(Calendar dataSaida,Calendar dataEntrada){
		
		int horaSaida = dataSaida.get(Calendar.HOUR_OF_DAY);
		int minutoSaida = dataSaida.get(Calendar.MINUTE);
		int minutosTotaisSaida = Converters.convertFromHourToMinute(horaSaida) + minutoSaida;
		
		int horaEntrada = dataEntrada.get(Calendar.HOUR_OF_DAY);
		int minutoEntrada = dataEntrada.get(Calendar.MINUTE);
		int minutosTotaisEntrada = Converters.convertFromHourToMinute(horaEntrada) + minutoEntrada;

		return new BigDecimal(minutosTotaisEntrada-minutosTotaisSaida);
	}
	
	private BigDecimal acrescentarAdicionalNoturno(BigDecimal minutosTrabalhados, int horaSaida,int horaEntrada,int minutoSaida,int minutoEntrada) {
		int horaSaidaAdicionalNoturno 	= 0;
		int minutosSaidaAdicionalNoturno = 0;
		if((horaSaida >= Constants.INICIO_HORARIO_NOTURNO && horaSaida < 24) || (horaSaida >=0 && horaSaida < Constants.FIM_HORARIO_NOTURNO)) {
			horaSaidaAdicionalNoturno = horaSaida-horaEntrada;
			minutosSaidaAdicionalNoturno = minutoSaida-minutoEntrada;
		}
		
		int minutosTotaisAdicionalNoturno = Converters.convertFromHourToMinute(horaSaidaAdicionalNoturno) + minutosSaidaAdicionalNoturno;
		
		return minutosTrabalhados.add(adicionalNoturno(new BigDecimal(minutosTotaisAdicionalNoturno)));
	}
	
	private BigDecimal adicionalNoturno(BigDecimal minutos) {
		BigDecimal adicional = BigDecimal.valueOf(0.2D);
		return minutos.multiply(adicional);
	}
	
	private BigDecimal adicionalSabado(BigDecimal minutos) {
		BigDecimal adicional = BigDecimal.valueOf(1.5D);
		return minutos.multiply(adicional);
	}
	
	private BigDecimal adicionalDomingo(BigDecimal minutos) {
		BigDecimal adicional = BigDecimal.valueOf(2D);
		return minutos.multiply(adicional);
	}

	@Override
	public void gerarHorasIntervalo(Ponto ponto) {
		if(ponto.getTipoRegistro().equals(Constants.TIPO_REGISTRO_ENTRADA)) {
			Ponto pontoSaidaAnterior = pontoRepository.findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(Constants.TIPO_REGISTRO_SAIDA, ponto.getFuncionario().getPis(), ponto.getDataHoraBatida());
			if(pontoSaidaAnterior != null) {
				Calendar dataBancoHoras =Constants.getMinHoraDia(ponto.getDataHoraBatida());
				BancoHoras bancoHoras = bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHoras(ponto.getFuncionario().getPis(), dataBancoHoras);
				
				BigDecimal minutosIntervalo = contabilizarTempoIntervalo(pontoSaidaAnterior.getDataHoraBatida(),ponto.getDataHoraBatida());
				
				if(bancoHoras == null) {
					bancoHoras = new BancoHoras();
					bancoHoras.setDataHoraBancoHoras(dataBancoHoras);
					bancoHoras.setFuncionario(ponto.getFuncionario());
					bancoHoras.setSaldoMinutosIntervalo(minutosIntervalo);
				}else {
					BigDecimal saldoIntervaloMinutos = bancoHoras.getSaldoMinutosIntervalo();
					saldoIntervaloMinutos = saldoIntervaloMinutos == null ? BigDecimal.ZERO : saldoIntervaloMinutos;
					bancoHoras.setSaldoMinutosIntervalo(saldoIntervaloMinutos.add(minutosIntervalo));
					
				}
				bancoHorasRepository.save(bancoHoras);
			}
		}
	}

	@Override
	public void gerarHorasNecessariasDescanco(Ponto ponto) {
		Calendar dataBancoHoras =Constants.getMinHoraDia(ponto.getDataHoraBatida());
		BancoHoras bancoHoras = bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHoras(ponto.getFuncionario().getPis(), dataBancoHoras);
		if(bancoHoras != null) {
			if(bancoHoras.getSaldoMinutos().compareTo(Constants.MINIMO_HORARIO_INTERVALO) > 0  && 0 > bancoHoras.getSaldoMinutos().compareTo(Constants.MAXIMO_HORARIO_INTERVALO)) {
				bancoHoras.setSaldoNecessarioDescanco(new BigDecimal(15));
			}else if(bancoHoras.getSaldoMinutos().compareTo(Constants.MAXIMO_HORARIO_INTERVALO) >= 0) {
				bancoHoras.setSaldoNecessarioDescanco(new BigDecimal(60));
			}
			bancoHorasRepository.save(bancoHoras);
		}
	}
}
