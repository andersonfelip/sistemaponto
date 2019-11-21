package com.andersonfelipe.pontoeletronico.app.controller;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.andersonfelipe.pontoeletronico.api.FuncionariosApi;
import com.andersonfelipe.pontoeletronico.api.model.Funcionario;
import com.andersonfelipe.pontoeletronico.api.model.Ponto;
import com.andersonfelipe.pontoeletronico.api.model.PontosPorDiaMes;
import com.andersonfelipe.pontoeletronico.app.constants.ModelMapperComponent;
import com.andersonfelipe.pontoeletronico.app.dto.DataFiltroDTO;
import com.andersonfelipe.pontoeletronico.app.dto.FuncionarioDTO;
import com.andersonfelipe.pontoeletronico.app.dto.PontoDTO;
import com.andersonfelipe.pontoeletronico.app.exceptions.ExceptionBadRequest;
import com.andersonfelipe.pontoeletronico.app.service.BancoHorasService;
import com.andersonfelipe.pontoeletronico.app.service.FuncionarioService;
import com.andersonfelipe.pontoeletronico.app.service.PontoService;
import com.andersonfelipe.pontoeletronico.app.util.Converters;

@RestController
public class FuncionarioController implements FuncionariosApi {

    private FuncionarioService funcionarioService;
    
    private PontoService pontoService;
    
    private BancoHorasService bancoHorasService;
    
    private ModelMapperComponent modelMapperComponent;
    
    public FuncionarioController(FuncionarioService funcionarioService,PontoService pontoService,BancoHorasService bancoHorasService,ModelMapperComponent modelMapperComponent) {
		super();
		this.funcionarioService = funcionarioService;
		this.pontoService = pontoService;
		this.bancoHorasService= bancoHorasService;
		this.modelMapperComponent = modelMapperComponent;
	}

	@Override
	public ResponseEntity<List<Funcionario>> listarFuncionarios() {
		List<FuncionarioDTO> funcionariosDTO = funcionarioService.listarFuncionarios();
		
		List<Funcionario> funcionarios = modelMapperComponent.modelMapper.map(funcionariosDTO, new TypeToken<List<Funcionario>>() {}.getType());
		modelMapperComponent.modelMapper.validate();
		return new ResponseEntity<>(funcionarios, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<PontosPorDiaMes> obterBatidasPorFuncionarioFiltro(@PathVariable String pisFuncionario,@PathVariable String dataBatida) {
		PontosPorDiaMes pontosPorDiaMes = new PontosPorDiaMes();
		List<PontoDTO> pontosDTO = pontoService.listarBatidasPorFuncionarioDataHoraBatida(pisFuncionario,dataBatida);	
		
		List<Ponto> pontos = modelMapperComponent.modelMapper.map(pontosDTO, new TypeToken<List<Ponto>>() {}.getType());
		modelMapperComponent.modelMapper.validate();
		
		DataFiltroDTO dataFiltro = Converters.convertFromStringMesOuDiaEmCalendar(dataBatida);
		BigDecimal saldoEmHoras = bancoHorasService.saldoHorasTrabalhadas(pisFuncionario, dataFiltro);
		BigDecimal saldoNecessarioDescanco = bancoHorasService.saldoNecessarioDescanco(pisFuncionario, dataFiltro);
		
		FuncionarioDTO funcionarioDTO = funcionarioService.obterFuncionario(pisFuncionario);
		
		Funcionario funcionario = modelMapperComponent.modelMapper.map(funcionarioDTO, Funcionario.class);
		modelMapperComponent.modelMapper.validate();
		
		pontosPorDiaMes.setPontos(pontos);
		pontosPorDiaMes.setFuncionario(funcionario);
		pontosPorDiaMes.setHorasTrabalhadas(Converters.convertFromBigDecimalToDouble(saldoEmHoras));
		pontosPorDiaMes.setMinutosNecessarioDescanco(saldoNecessarioDescanco);
		return new ResponseEntity<>(pontosPorDiaMes, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Funcionario> obterFuncionario(@PathVariable String pisFuncionario) {
		FuncionarioDTO funcionarioDTO = funcionarioService.obterFuncionario(pisFuncionario);
		
		Funcionario funcionario = modelMapperComponent.modelMapper.map(funcionarioDTO, Funcionario.class);
		modelMapperComponent.modelMapper.validate();
		return new ResponseEntity<>(funcionario, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<Ponto> incluirBatida(@PathVariable String pisFuncionario,@RequestBody Ponto body) {
		if(body == null) {
			throw new ExceptionBadRequest("Não foi possível registrar ponto.Favor preencha os dados necessários.");
		}
		
		body.setPisFuncionario(pisFuncionario);
		com.andersonfelipe.pontoeletronico.app.domain.Ponto ponto = modelMapperComponent.modelMapper.map(body,
				com.andersonfelipe.pontoeletronico.app.domain.Ponto.class);
		modelMapperComponent.modelMapper.validate();
		
		com.andersonfelipe.pontoeletronico.app.domain.Ponto pontoSalvo = pontoService.save(ponto);
		
		Ponto response = modelMapperComponent.modelMapper.map(pontoSalvo,Ponto.class);
		modelMapperComponent.modelMapper.validate();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<Ponto>> obterBatidasPorFuncionario(@PathVariable String pisFuncionario) {
		List<PontoDTO> pontosDTO = pontoService.listarBatidasPorFuncionarioDataHoraBatida(pisFuncionario,null);	
		
		
		List<Ponto> pontos = modelMapperComponent.modelMapper.map(pontosDTO, new TypeToken<List<Ponto>>() {}.getType());
		modelMapperComponent.modelMapper.validate();
		return new ResponseEntity<>(pontos, HttpStatus.OK);
	}
}