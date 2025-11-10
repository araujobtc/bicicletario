package com.bikeunirio.bicicletario.aluguel.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;

@Service
public class FuncionarioService {
	
	FuncionarioRepository funcionarioRepository;
	
	public List<Funcionario> getAllFuncionarios(){
		return funcionarioRepository.findAll();
	}
	
    public Funcionario createFuncionario(Funcionario funcionario) {
        return funcionarioRepository.save(funcionario);
    }
	
}