package com.andersonfelipe.pontoeletronico.app.constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;

public class ModelConverter {
	
	public static Converter<Boolean, String> convertStatus = new AbstractConverter<Boolean, String>() {
		@Override
		protected String convert(Boolean source) {
			if(source) {
				return "Ativo";
			}else {
				return "Inativo";
			}
		}
	};
	
	public static Converter<String, String> convertTipoRegistro = new AbstractConverter<String, String>() {
		@Override
		protected String convert(String source) {
			if(source.equals(Constants.TIPO_REGISTRO_ENTRADA)) {
				return "Entrada";
			}else {
				return "Saída";
			}
		}
	};
	
	public static Converter<Calendar, String> converFromCalendarToString = new AbstractConverter<Calendar, String>() {
		@Override
		protected String convert(Calendar source) {
			SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_FULL_BR);	
			return df.format(source.getTime());			 
		}

	};
	
	public static Converter<String, Calendar> convertFromStringToCalendar = new AbstractConverter<String, Calendar>() {
		@Override
		protected Calendar convert(String source) {
			SimpleDateFormat df = new SimpleDateFormat(Constants.DATE_FORMAT_FULL_BR);
			df.setLenient(false);
			try {
				Date date = df.parse(source);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				return calendar;
			} catch (ParseException e) {
				return null;
			}
		}
	};
}
