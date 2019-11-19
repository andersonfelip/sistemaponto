package com.andersonfelipe.pontoeletronico.app.domain;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class BancoHoras {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Calendar dataHoraBancoHoras;
    @Column
    private BigDecimal saldoMinutos;
    @Column
    private BigDecimal saldoMinutosIntervalo;
    @Column
    private BigDecimal saldoNecessarioDescanco;
    @ManyToOne
    private Funcionario funcionario;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Funcionario getFuncionario() {
		return funcionario;
	}
	public void setFuncionario(Funcionario funcionario) {
		this.funcionario = funcionario;
	}
	public Calendar getDataHoraBancoHoras() {
		return dataHoraBancoHoras;
	}
	public void setDataHoraBancoHoras(Calendar dataHoraBancoHoras) {
		this.dataHoraBancoHoras = dataHoraBancoHoras;
	}
	public BigDecimal getSaldoMinutos() {
		return saldoMinutos;
	}
	public void setSaldoMinutos(BigDecimal saldoMinutos) {
		this.saldoMinutos = saldoMinutos;
	}
	public BigDecimal getSaldoMinutosIntervalo() {
		return saldoMinutosIntervalo;
	}
	public void setSaldoMinutosIntervalo(BigDecimal saldoMinutosIntervalo) {
		this.saldoMinutosIntervalo = saldoMinutosIntervalo;
	}
	public BigDecimal getSaldoNecessarioDescanco() {
		return saldoNecessarioDescanco;
	}
	public void setSaldoNecessarioDescanco(BigDecimal saldoNecessarioDescanco) {
		this.saldoNecessarioDescanco = saldoNecessarioDescanco;
	}
}
