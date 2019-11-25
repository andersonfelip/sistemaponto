package com.andersonfelipe.pontoeletronico.app.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.MediaType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Base64Utils;

import com.andersonfelipe.pontoeletronico.api.model.Funcionario;
import com.andersonfelipe.pontoeletronico.api.model.Ponto;
import com.andersonfelipe.pontoeletronico.api.model.PontosPorDiaMes;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FuncionarioControllerTest {

	@LocalServerPort
	private int port = 80;
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = basicAuth();
	
	private static final String host = "http://localhost:";

	@Test
	public void testeCriarBatida1() {
		Funcionario func = criarFuncionario();
		
		Ponto ponto = new Ponto();
		ponto.setDataHoraBatida("19/11/2019 09:00:00");
		
		HttpEntity<Ponto> entity = new HttpEntity<Ponto>(ponto, headers);
		ResponseEntity<String> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/incluirBatida"), HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}
	
	@Test
	public void testeCriarBatida2() {
		Funcionario func = criarFuncionario();
		
		Ponto ponto = new Ponto();
		ponto.setDataHoraBatida("19/11/2019 12:00:00");
		
		HttpEntity<Ponto> entity = new HttpEntity<Ponto>(ponto, headers);
		ResponseEntity<String> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/incluirBatida"), HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}
	
	@Test
	public void testeCriarBatida3() {
		Funcionario func = criarFuncionario();
		
		Ponto ponto = new Ponto();
		ponto.setDataHoraBatida("19/11/2019 13:00:00");
		
		HttpEntity<Ponto> entity = new HttpEntity<Ponto>(ponto, headers);
		ResponseEntity<String> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/incluirBatida"), HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}
	
	@Test
	public void testeCriarBatida4() {
		Funcionario func = criarFuncionario();
		
		Ponto ponto = new Ponto();
		ponto.setDataHoraBatida("19/11/2019 18:00:00");
		
		HttpEntity<Ponto> entity = new HttpEntity<Ponto>(ponto, headers);
		ResponseEntity<String> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/incluirBatida"), HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
	}
	
	@Test
	public void testeListarFuncionarios() {
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Funcionario[]> response = restTemplate.exchange(criarUrlPorta("/funcionarios"), HttpMethod.GET, entity, Funcionario[].class);
		Funcionario[] funcionarios = response.getBody();
		
		assertNotNull(funcionarios);
	}
	
	@Test
	public void testeGetFuncionario() {
		Funcionario func = criarFuncionario();
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Funcionario> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()), HttpMethod.GET, entity, Funcionario.class);
		Funcionario funcionario = response.getBody();
		
		assertEquals(func, funcionario);
	}
	
	@Test
	public void testeGetBatidas() {
		Funcionario func = criarFuncionario();
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<Ponto[]> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/batidas"), HttpMethod.GET, entity, Ponto[].class);
		Ponto[] pontos = response.getBody();
		
		assertNotNull(pontos);
	}
	
	@Test
	public void testeBatidasPorFiltro() {
		Funcionario func = criarFuncionario();
		
		String filtroMesDia = "2019-11";
		
		HttpEntity<String> entity = new HttpEntity<String>(null, headers);
		ResponseEntity<PontosPorDiaMes> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/batidas/"+filtroMesDia), HttpMethod.GET, entity, PontosPorDiaMes.class);
		PontosPorDiaMes pontosPorDiaMes= response.getBody();
		
		assertNotNull(pontosPorDiaMes);
	}
	
	private HttpHeaders basicAuth() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64Utils.encodeToString("123456:123456".getBytes()));
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON);
		return headers;
	}

	private Funcionario criarFuncionario() {
		Funcionario f = new Funcionario();
		f.setEmail("anderson_felipe@hotmail.com");
		f.setNome("Anderson Felipe de Melo Rocha");
		f.setPis("123456");
		f.setStatus("Ativo");
		return f;
	}
	
	private String criarUrlPorta(String uri) {
		return host + port + uri;
	}
}
