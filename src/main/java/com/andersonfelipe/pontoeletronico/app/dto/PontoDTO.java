package com.andersonfelipe.pontoeletronico.app.dto;

import java.util.Calendar;

public class PontoDTO {
	
	private Long id;
	private Calendar dataHoraBatida;
    private String tipoRegistro;
    private String pisFuncionario;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Calendar getDataHoraBatida() {
		return dataHoraBatida;
	}
	public void setDataHoraBatida(Calendar dataHoraBatida) {
		this.dataHoraBatida = dataHoraBatida;
	}
	public String getTipoRegistro() {
		return tipoRegistro;
	}
	public void setTipoRegistro(String tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	public String getPisFuncionario() {
		return pisFuncionario;
	}
	public void setPisFuncionario(String pisFuncionario) {
		this.pisFuncionario = pisFuncionario;
	}
}
