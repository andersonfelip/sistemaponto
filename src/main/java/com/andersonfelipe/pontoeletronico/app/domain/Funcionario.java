package com.andersonfelipe.pontoeletronico.app.domain;

import javax.persistence.*;
import java.util.List;

@Entity
public class Funcionario {

	@Id
    private String pis;
    @Column
    private String senha;
    @Column
    private String email;
    @Column
    private String nome;
    @Column
    private Boolean status;
    

    @OneToMany(mappedBy = "funcionario")
    private List<Ponto> pontos;
    
    @OneToMany(mappedBy = "funcionario")
    private List<BancoHoras> bancoHoras;

	public String getPis() {
		return pis;
	}

	public void setPis(String pis) {
		this.pis = pis;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Ponto> getPontos() {
		return pontos;
	}

	public void setPontos(List<Ponto> pontos) {
		this.pontos = pontos;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public List<BancoHoras> getBancoHoras() {
		return bancoHoras;
	}

	public void setBancoHoras(List<BancoHoras> bancoHoras) {
		this.bancoHoras = bancoHoras;
	}
	
}
