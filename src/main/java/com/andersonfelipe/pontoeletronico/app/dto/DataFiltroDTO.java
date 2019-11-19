package com.andersonfelipe.pontoeletronico.app.dto;

import java.util.Calendar;

public class DataFiltroDTO {
	Calendar dataInicio;
	Calendar dataFim;
	
	public Calendar getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Calendar dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Calendar getDataFim() {
		return dataFim;
	}
	public void setDataFim(Calendar dataFim) {
		this.dataFim = dataFim;
	}
	
}
