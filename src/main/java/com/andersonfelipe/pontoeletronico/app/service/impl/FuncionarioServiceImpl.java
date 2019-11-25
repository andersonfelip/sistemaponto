package com.andersonfelipe.pontoeletronico.app.service.impl;

import java.util.List;

import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.andersonfelipe.pontoeletronico.app.constants.ModelMapperComponent;
import com.andersonfelipe.pontoeletronico.app.domain.Funcionario;
import com.andersonfelipe.pontoeletronico.app.dto.FuncionarioDTO;
import com.andersonfelipe.pontoeletronico.app.exceptions.ExceptionNotFound;
import com.andersonfelipe.pontoeletronico.app.repository.FuncionarioRepository;
import com.andersonfelipe.pontoeletronico.app.service.FuncionarioService;

@Service
@Transactional
public class FuncionarioServiceImpl implements FuncionarioService {
	
	@Autowired
	private FuncionarioRepository funcionarioRepository; 

	@Override
	public List<FuncionarioDTO> listarFuncionarios() {
		List<Funcionario> funcionarios = funcionarioRepository.findAll();
		
		List<FuncionarioDTO> funcionariosDTO = ModelMapperComponent.modelMapper.map(funcionarios, new TypeToken<List<FuncionarioDTO>>() {}.getType());
		ModelMapperComponent.modelMapper.validate();
		return funcionariosDTO;
	}

	@Override
	public FuncionarioDTO obterFuncionario(String pisFuncionario) {
		Funcionario funcionario = funcionarioRepository.findByPis(pisFuncionario);
		
		if(funcionario == null) {
			throw new ExceptionNotFound("Este funcionário não existe");
		}
		
		FuncionarioDTO funcionariosDTO = ModelMapperComponent.modelMapper.map(funcionario, FuncionarioDTO.class);
		ModelMapperComponent.modelMapper.validate();
		return funcionariosDTO;
	}
}
