package com.andersonfelipe.pontoeletronico.app.security;

import static java.util.Collections.emptyList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.andersonfelipe.pontoeletronico.app.domain.Funcionario;
import com.andersonfelipe.pontoeletronico.app.repository.FuncionarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private FuncionarioRepository funcionarioRepository;

    public UserDetailsServiceImpl(FuncionarioRepository funcionarioRepository) {
		super();
		this.funcionarioRepository = funcionarioRepository;
	}

	@Override
    public UserDetails loadUserByUsername(String pis) {
        Funcionario applicationUser = funcionarioRepository.findByPis(pis);
        if (applicationUser == null) {
            throw new UsernameNotFoundException(pis);
        } 
        return new User(applicationUser.getPis(), applicationUser.getSenha(), applicationUser.getStatus(), true, true, true,  emptyList());
    }
}
