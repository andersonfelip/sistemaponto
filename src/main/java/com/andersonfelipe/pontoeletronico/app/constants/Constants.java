package com.andersonfelipe.pontoeletronico.app.constants;

import java.math.BigDecimal;
import java.util.Calendar;

public class Constants {
	public static final String DATE_FORMAT_BR = "dd/MM/yyyy";
	public static final String DATE_FORMAT_EUA = "yyyy-MM-dd";
	public static final String DATE_FORMAT_FULL_BR = "dd/MM/yyyy HH:mm";
	public static final String TIPO_REGISTRO_ENTRADA = "E";
	public static final String TIPO_REGISTRO_SAIDA = "S";
	
	public static final int INICIO_HORARIO_NOTURNO = 22;
	public static final int FIM_HORARIO_NOTURNO = 6;
	
	public static final BigDecimal MINIMO_HORARIO_INTERVALO = new BigDecimal(240);
	public static final BigDecimal MAXIMO_HORARIO_INTERVALO = new BigDecimal(360);
	
	
	public static final Calendar getMinHoraDia(Calendar dataHoraBatida) {
		Calendar dataHoraInicio = Calendar.getInstance();
		dataHoraInicio.set(Calendar.YEAR, dataHoraBatida.get(Calendar.YEAR));
		dataHoraInicio.set(Calendar.MONTH, dataHoraBatida.get(Calendar.MONTH));
		dataHoraInicio.set(Calendar.DAY_OF_MONTH, dataHoraBatida.get(Calendar.DAY_OF_MONTH));
		dataHoraInicio.set(Calendar.HOUR_OF_DAY, 0);
		dataHoraInicio.set(Calendar.MINUTE,0);
		dataHoraInicio.set(Calendar.SECOND,0);
		
		return dataHoraInicio;
	}
	
	public static final Calendar getMaxHoraDia(Calendar dataHoraBatida) {
		Calendar dataHoraFim = Calendar.getInstance();
		dataHoraFim.set(Calendar.YEAR, dataHoraBatida.get(Calendar.YEAR));
		dataHoraFim.set(Calendar.MONTH, dataHoraBatida.get(Calendar.MONTH));
		dataHoraFim.set(Calendar.DAY_OF_MONTH, dataHoraBatida.get(Calendar.DAY_OF_MONTH));
		dataHoraFim.set(Calendar.HOUR_OF_DAY, 23);
		dataHoraFim.set(Calendar.MINUTE,59);
		dataHoraFim.set(Calendar.SECOND,59);
		return dataHoraFim;
	}
}
