package com.andersonfelipe.pontoeletronico.app.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
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

import com.andersonfelipe.pontoeletronico.app.constants.Constants;
import com.andersonfelipe.pontoeletronico.app.domain.BancoHoras;
import com.andersonfelipe.pontoeletronico.app.domain.Funcionario;
import com.andersonfelipe.pontoeletronico.app.domain.Ponto;
import com.andersonfelipe.pontoeletronico.app.dto.DataFiltroDTO;
import com.andersonfelipe.pontoeletronico.app.repository.BancoHorasRepository;
import com.andersonfelipe.pontoeletronico.app.repository.PontoRepository;
import com.andersonfelipe.pontoeletronico.app.service.impl.BancoHorasServiceImpl;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class BancoHorasServiceImplTest {
	
	@MockBean
	private BancoHorasRepository bancoHorasRepository;
	
	@MockBean
	private PontoRepository pontoRepository;
	
	@Autowired
    private BancoHorasService bancoHorasService;
	
	private Funcionario func;
	
	private BancoHoras bancoHoras;
	
	private Ponto pontoEntrada;
	
	private Ponto pontoSaida;
	
	private Calendar dataHoraInicio;
	
	private Calendar dataHoraFim;
	
	@TestConfiguration
    static class BancoHorasServiceImplTestContextConfiguration {
  
        @Bean
        public BancoHorasService bancoHorasService() {
            return new BancoHorasServiceImpl();
        }
    }
	
    @Before
    public void before(){
    	dataHoraInicio = criarData(20,11,2019,10,0,0);
    	dataHoraFim = criarData(21,11,2019,12,0,0);
    	
    	func = criarFuncionario();
    	bancoHoras = criarBancoHoras();
    	pontoEntrada = criarPontoEntrada();
    	pontoSaida = criarPontoSaida();
    	
    	Mockito.when(bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHoras(Mockito.anyString(),Mockito.any(Calendar.class))).thenReturn(bancoHoras);
    	Mockito.when(bancoHorasRepository.findByFuncionarioPisAndDataHoraBancoHorasBetween(func.getPis(),dataHoraInicio,dataHoraFim)).thenReturn(criarListaBancoHoras());
    	Mockito.when(pontoRepository.findFirstByTipoRegistroAndFuncionarioPisAndDataHoraBatidaLessThanOrderByDataHoraBatidaDesc(Mockito.anyString(), Mockito.anyString(), Mockito.any(Calendar.class))).thenReturn(pontoEntrada);
    	Mockito.when(bancoHorasRepository.save(Mockito.any(BancoHoras.class))).thenReturn(bancoHoras);
    }
	
	@Test
	public void testeFindByFuncionarioPisAndDataHoraBancoHoras() {
		Funcionario func = criarFuncionario();
		
		BancoHoras result = bancoHorasService.findByFuncionarioPisAndDataHoraBancoHoras(func.getPis(), dataHoraInicio);
		assertEquals(BigDecimal.valueOf(60),result.getSaldoMinutos());
	}
	
	@Test
	public void testefindByFuncionarioPisAndDataHoraBancoHorasBetween() {
		Funcionario func = criarFuncionario();
		
		List<BancoHoras> result = bancoHorasService.findByFuncionarioPisAndDataHoraBancoHorasBetween(func.getPis(), dataHoraInicio,dataHoraFim);
		assertNotNull(result);
		
		assertEquals(BigDecimal.valueOf(60), result.get(0).getSaldoMinutos());
	}
	
	@Test
	public void testeSaldoNecessarioDescanco() {
		DataFiltroDTO dataFiltro = new DataFiltroDTO();
		dataFiltro.setDataInicio(dataHoraInicio);
		dataFiltro.setDataFim(dataHoraFim);
		
		BigDecimal saldoNecessarioDescanco = bancoHorasService.saldoNecessarioDescanco(func.getPis(), dataFiltro);
		assertEquals(BigDecimal.valueOf(0), saldoNecessarioDescanco);
	}
	
	@Test
	public void testeGerarHorasTrabalhadas() {
		bancoHorasService.gerarHorasTrabalhadas(pontoSaida);
	}
	
	@Test
	public void testeSaldoHorasTrabalhadas() {
		DataFiltroDTO dataFiltro = new DataFiltroDTO();
		dataFiltro.setDataInicio(dataHoraInicio);
		dataFiltro.setDataFim(dataHoraFim);
		
		BigDecimal saldoHorasTrabalhadas = bancoHorasService.saldoHorasTrabalhadas(func.getPis(), dataFiltro);
		assertEquals(BigDecimal.valueOf(1), BigDecimal.valueOf(saldoHorasTrabalhadas.longValue()));
	}
	
	@Test
	public void testeGerarHorasNecessariasDescanco() {
		bancoHorasService.gerarHorasNecessariasDescanco(pontoSaida);
	}
	
	@Test
	public void testeGerarHorasIntervalo() {
		bancoHorasService.gerarHorasIntervalo(pontoEntrada);
	}
	
	private BancoHoras criarBancoHoras() {
		BancoHoras b = new BancoHoras();
		b.setDataHoraBancoHoras(Calendar.getInstance());
		b.setFuncionario(criarFuncionario());
		b.setSaldoMinutos(BigDecimal.valueOf(60));
		b.setSaldoMinutosIntervalo(BigDecimal.valueOf(60));
		b.setSaldoNecessarioDescanco(BigDecimal.valueOf(0));
		return b;
	}
	
	private List<BancoHoras> criarListaBancoHoras() {
		List<BancoHoras> bancoHorasLista = new ArrayList<BancoHoras>();
		BancoHoras b = criarBancoHoras();
		bancoHorasLista.add(b);
		return bancoHorasLista;
	}
	
	private Ponto criarPontoEntrada() {
		Ponto p = new Ponto();
		p.setDataHoraBatida(dataHoraInicio);
		p.setFuncionario(func);
		p.setTipoRegistro(Constants.TIPO_REGISTRO_ENTRADA);
		return p;
	}
	
	private Ponto criarPontoSaida() {
		Ponto p = new Ponto();
		p.setDataHoraBatida(dataHoraFim);
		p.setFuncionario(func);
		p.setTipoRegistro(Constants.TIPO_REGISTRO_SAIDA);
		return p;
	}
	
	private Funcionario criarFuncionario() {
		Funcionario f = new Funcionario();
		f.setEmail("anderson_felipe@hotmail.com");
		f.setNome("Anderson Felipe de Melo Rocha");
		f.setPis("123456");
		return f;
	}
	
	private Calendar criarData(int dia, int mes, int ano, int hora, int minuto, int segundo) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.DAY_OF_MONTH, dia);
    	c.set(Calendar.MONTH,mes);
    	c.set(Calendar.YEAR,ano);
    	c.set(Calendar.HOUR_OF_DAY,hora);
    	c.set(Calendar.MINUTE, minuto);
    	c.set(Calendar.SECOND, segundo);
    	c.set(Calendar.MILLISECOND, 0);
    	
    	return c;
	}
}
