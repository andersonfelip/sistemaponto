package com.andersonfelipe.pontoeletronico.app.dto;

public class FuncionarioDTO {
	private String nome;
	private String pis;
	private String email;
	private Boolean status;
	
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getPis() {
		return pis;
	}
	public void setPis(String pis) {
		this.pis = pis;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
}
