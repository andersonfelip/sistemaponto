package com.andersonfelipe.pontoeletronico.app.controller;

import static org.junit.Assert.assertEquals;

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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FuncionarioControllerTest {

	@LocalServerPort
	private int port = 80;
	TestRestTemplate restTemplate = new TestRestTemplate();
	HttpHeaders headers = basicAuth();

	private static final String host = "http://localhost:";

	@Test
	public void testeCriarBatida() throws Exception {
		Funcionario func = criarFuncionario();
		
		Ponto ponto = new Ponto();
		ponto.setDataHoraBatida("19/11/2019 09:00:00");
		
		HttpEntity<Ponto> entity = new HttpEntity<Ponto>(ponto, headers);
		ResponseEntity<String> response = restTemplate.exchange(criarUrlPorta("/funcionarios/"+func.getPis()+"/incluirBatida"), HttpMethod.POST, entity, String.class);
		assertEquals(HttpStatus.OK,response.getStatusCode());
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
