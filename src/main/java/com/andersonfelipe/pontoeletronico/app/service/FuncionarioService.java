package com.andersonfelipe.pontoeletronico.app.service;

import java.util.List;

import com.andersonfelipe.pontoeletronico.app.dto.FuncionarioDTO;

public interface FuncionarioService {
    public List<FuncionarioDTO> listarFuncionarios();
    
    public FuncionarioDTO obterFuncionario(String pisFuncionario);
}
