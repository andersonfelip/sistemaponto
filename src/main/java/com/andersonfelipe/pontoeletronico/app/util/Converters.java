package com.andersonfelipe.pontoeletronico.app.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.andersonfelipe.pontoeletronico.app.constants.Constants;
import com.andersonfelipe.pontoeletronico.app.dto.DataFiltroDTO;

public class Converters {

	public static Calendar convertFromStringToCalendarFormatBr(String source) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_BR);

		df.setLenient(false);

		try {
			Date date = null;
			date = df.parse(source);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static Calendar convertFromStringToCalendarFormatEua(String source) {
		SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_EUA);

		df.setLenient(false);

		try {
			Date date = null;
			date = df.parse(source);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		} catch (ParseException e) {
			return null;
		}
	}
	
	public static int convertFromHourToMinute(int hora) {
		return (hora * 60);
	}
	
	public static BigDecimal convertFromMinuteToHour(BigDecimal minutos) {
		if(minutos != BigDecimal.ZERO) {
			return minutos.divide(new BigDecimal(60), 2, RoundingMode.HALF_DOWN);
		}else {
			return BigDecimal.ZERO;
		}
		
	}
	
	public static DataFiltroDTO convertFromStringMesOuDiaEmCalendar(String dataBatida) {
		DataFiltroDTO dataFiltro = new DataFiltroDTO();
		String dataBatidaInicio = "";
		String dataBatidaFim = "";
		if(dataBatida.length() == 7) {
			
			StringBuilder sbPrimeiroDiaMes = new StringBuilder();
			sbPrimeiroDiaMes.append(dataBatida);
			sbPrimeiroDiaMes.append("-01");
			
			Calendar cal = Converters.convertFromStringToCalendarFormatEua(sbPrimeiroDiaMes.toString());
			int ultimoDiaMes = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			
			StringBuilder sbUltimoDiaMes = new StringBuilder();
			sbUltimoDiaMes.append(dataBatida);
			sbUltimoDiaMes.append("-");
			sbUltimoDiaMes.append(ultimoDiaMes);
			
			dataBatidaInicio = sbPrimeiroDiaMes.toString();
			dataBatidaFim = sbUltimoDiaMes.toString();
			
		}else {
			dataBatidaInicio = dataBatida;
			dataBatidaFim = dataBatida;
		}
		Calendar dataHoraInicio = Constants.getMinHoraDia(Converters.convertFromStringToCalendarFormatEua(dataBatidaInicio));
		Calendar dataHoraFim = Constants.getMaxHoraDia(Converters.convertFromStringToCalendarFormatEua(dataBatidaFim));
		dataFiltro.setDataFim(dataHoraFim);
		dataFiltro.setDataInicio(dataHoraInicio);
		return dataFiltro;
	}
	
	public static Double convertFromBigDecimalToDouble(BigDecimal source) {
		return source != null ? source.doubleValue() : null;
	}
	
}
