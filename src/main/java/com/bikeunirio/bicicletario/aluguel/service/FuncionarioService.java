package com.bikeunirio.bicicletario.aluguel.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;

@Service
public class FuncionarioService {

	FuncionarioRepository funcionarioRepository;
	
	public FuncionarioService(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

	/**/

	public boolean existsById(long id) {
		return funcionarioRepository.existsById(id);
	}

	/**/

	public List<Funcionario> getAllFuncionarios() {
		return funcionarioRepository.findAll();
	}

	public Funcionario createFuncionario(FuncionarioDTO funcionarioDTO) {
		Funcionario funcionario = new Funcionario();

		// Copia todos os campos do DTO para a entidade existente,
		// exceto id e matricula, que não devem ser alterados
		BeanUtils.copyProperties(funcionarioDTO, funcionario, "id", "matricula");

		funcionario.setMatricula(Long.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"))));
		
		return funcionarioRepository.save(funcionario);
	}

	public Optional<Funcionario> readFuncionario(long id) {
		return funcionarioRepository.findById(id);
	}

	public Optional<Funcionario> updateFuncionario(long id, FuncionarioDTO funcionarioDTO) {
		return funcionarioRepository.findById(id).map(funcionario -> {
			BeanUtils.copyProperties(funcionarioDTO, funcionario, "id", "matricula");
			return funcionarioRepository.save(funcionario);
		});
	}
	
	public void deleteFuncionario(long id) {
        Optional<Funcionario> funcionario = funcionarioRepository.findById(id);
        
        if (funcionario.isEmpty()) {
            return;
        }
        funcionarioRepository.delete(funcionario.get());
	}

}