package com.andersonfelipe.pontoeletronico.app.domain;

import java.util.Calendar;

import javax.persistence.*;

@Entity
public class Ponto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Funcionario funcionario;
    @Column
    private Calendar dataHoraBatida;
    @Column
    private String tipoRegistro;

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

	public Funcionario getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
}
