package com.andersonfelipe.pontoeletronico.app.service;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.andersonfelipe.pontoeletronico.app.domain.Funcionario;
import com.andersonfelipe.pontoeletronico.app.dto.FuncionarioDTO;
import com.andersonfelipe.pontoeletronico.app.repository.FuncionarioRepository;
import com.andersonfelipe.pontoeletronico.app.service.impl.FuncionarioServiceImpl;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class FuncionarioServiceImplTest {
	
	@MockBean
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
    private FuncionarioService funcionarioService;
	
	private Funcionario func;
	
	@TestConfiguration
    static class FuncionarioServiceImplTestContextConfiguration {
  
        @Bean
        public FuncionarioService funcionarioService() {
            return new FuncionarioServiceImpl();
        }
    }
	
    @Before
    public void before(){
    	func = criarFuncionario();
    	
    	Mockito.when(funcionarioRepository.findAll()).thenReturn(criarListaFuncionarios());
    	Mockito.when(funcionarioRepository.findByPis(func.getPis())).thenReturn(criarFuncionario());
    }
	
	@Test
	public void testeFindByFuncionarioPisAndDataHoraBancoHoras() {
		Funcionario func = criarFuncionario();
		
		List<FuncionarioDTO> result = funcionarioService.listarFuncionarios();
		assertEquals(func.getPis(),result.get(0).getPis());
	}
	
	private List<Funcionario> criarListaFuncionarios() {
		List<Funcionario> funcionarioLista = new ArrayList<Funcionario>();
		Funcionario f = criarFuncionario();
		funcionarioLista.add(f);
		return funcionarioLista;
	}
	
	
	private Funcionario criarFuncionario() {
		Funcionario f = new Funcionario();
		f.setEmail("anderson_felipe@hotmail.com");
		f.setNome("Anderson Felipe de Melo Rocha");
		f.setPis("123456");
		return f;
	}
	
}
