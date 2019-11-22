package com.andersonfelipe.pontoeletronico.app.constants;

import javax.annotation.PostConstruct;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import com.andersonfelipe.pontoeletronico.app.dto.FuncionarioDTO;
import com.andersonfelipe.pontoeletronico.app.dto.PontoDTO;

@Component
public class ModelMapperComponent {
	
	public static final ModelMapper modelMapper= new ModelMapper();
	
	@PostConstruct
	private void configureMapper() {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        
        modelMapper.addMappings(
                new PropertyMap<com.andersonfelipe.pontoeletronico.app.domain.Funcionario, FuncionarioDTO>() {
                    @Override
                    protected void configure() {
                    	map().setEmail(source.getEmail());
                    	map().setNome(source.getNome());
                    	map().setPis(source.getPis());
                    	map().setStatus(source.getStatus());
                    }
                });
        
        modelMapper.addMappings(
                new PropertyMap<FuncionarioDTO, com.andersonfelipe.pontoeletronico.api.model.Funcionario>() {
                    @Override
                    protected void configure() { 
                    	map().setEmail(source.getEmail());
                    	map().setNome(source.getNome());
                    	map().setPis(source.getPis());
                    	when(Conditions.isNotNull()).using(ModelConverter.convertStatus).map(source.getStatus()).setStatus(null);
                    }
                });
        
        modelMapper.addMappings(
                new PropertyMap<PontoDTO, com.andersonfelipe.pontoeletronico.api.model.Ponto>() {
                    @Override
                    protected void configure() { 
                    	map().setId(source.getId());
                    	when(Conditions.isNotNull()).using(ModelConverter.converFromCalendarToString).map(source.getDataHoraBatida()).setDataHoraBatida(null);
                    	map().setTipoRegistro(source.getTipoRegistro());
                    	map().setPisFuncionario(source.getPisFuncionario());
                    }
                });
        
        modelMapper.addMappings(
                new PropertyMap<com.andersonfelipe.pontoeletronico.app.domain.Ponto, PontoDTO>() {
                    @Override
                    protected void configure() { 
                    	map().setId(source.getId());
                    	map().setDataHoraBatida(source.getDataHoraBatida());
                    	when(Conditions.isNotNull()).using(ModelConverter.convertTipoRegistro).map(source.getTipoRegistro()).setTipoRegistro(null);
                    	map().setPisFuncionario(source.getFuncionario().getPis());
                    }
                });
        
        modelMapper.addMappings(
                new PropertyMap<com.andersonfelipe.pontoeletronico.app.domain.Ponto, com.andersonfelipe.pontoeletronico.api.model.Ponto>() {
                    @Override
                    protected void configure() { 
                    	map().setId(source.getId());
                    	when(Conditions.isNotNull()).using(ModelConverter.converFromCalendarToString).map(source.getDataHoraBatida()).setDataHoraBatida(null);
                    	when(Conditions.isNotNull()).using(ModelConverter.convertTipoRegistro).map(source.getTipoRegistro()).setTipoRegistro(null);
                    	map().setPisFuncionario(source.getFuncionario().getPis());
                    }
                });
        
       modelMapper.addMappings(
                new PropertyMap<com.andersonfelipe.pontoeletronico.api.model.Ponto, com.andersonfelipe.pontoeletronico.app.domain.Ponto>() {
                    @Override
                    protected void configure() { 
                    	map().setId(source.getId());
                    	when(Conditions.isNotNull()).using(ModelConverter.convertFromStringToCalendar).map(source.getDataHoraBatida()).setDataHoraBatida(null);
                    	skip().setTipoRegistro(null);
                    	when(Conditions.isNotNull()).map().getFuncionario().setPis(source.getPisFuncionario());
                    	skip().setFuncionario(null);
                    }
                });
	}
}
