package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

	@Autowired
	private FuncionarioService funcionarioService;

	@GetMapping
	public ResponseEntity<List<Funcionario>> getAllFuncionarios() {
		List<Funcionario> funcionarios = funcionarioService.getAllFuncionarios();
		return ResponseEntity.ok(funcionarios);
	}
	
	@PostMapping
	public ResponseEntity<?> createFuncionario(@Valid @RequestBody FuncionarioDTO funcionarioDTO) {

	    // Valida se senha e confirmação são iguais
	    if (!funcionarioDTO.getSenha().equals(funcionarioDTO.getConfirmacaoSenha())) {
			return GlobalExceptionHandler.unprocessableEntity("Senha e confirmação de senha não coincidem");
	    }
	    Funcionario funcionario = funcionarioService.createFuncionario(funcionarioDTO);

	    return ResponseEntity.ok(funcionario); // 200
	}


	@GetMapping("/{idFuncionario}")
	public ResponseEntity<?> readFuncionario(@PathVariable Long idFuncionario) {
		Optional<Funcionario> funcionario = funcionarioService.readFuncionario(idFuncionario);

		if (funcionario.isPresent()) {
			return ResponseEntity.ok(funcionario.get()); // 200
		} else {
			return GlobalExceptionHandler.notFound("Funcionário não encontrado");
		}
	}

	@PutMapping("/{idFuncionario}")
	public ResponseEntity<?> updateFuncionario(@PathVariable Long idFuncionario, @RequestBody @Valid FuncionarioDTO funcionarioDTO) {

		// Valida se senha e confirmação são iguais
		if (!funcionarioDTO.getSenha().equals(funcionarioDTO.getConfirmacaoSenha())) {
			return GlobalExceptionHandler.unprocessableEntity("Senha e confirmação de senha não coincidem");
		}

		Optional<Funcionario> atualizado = funcionarioService.updateFuncionario(idFuncionario, funcionarioDTO);

		if (atualizado.isPresent()) {
			return ResponseEntity.ok(atualizado.get()); // 200
		} else {
			return GlobalExceptionHandler.notFound("Funcionário não encontrado");
		}
	}

    @DeleteMapping("/{idFuncionario}")
    public ResponseEntity<?> deleteFuncionario(@PathVariable @Min(value = 1, message = "O ID do funcionário deve ser maior que zero") Long idFuncionario) {
        if (funcionarioService.existsById(idFuncionario)) {
            funcionarioService.deleteFuncionario(idFuncionario);
            return ResponseEntity.ok("Dados removidos"); // 200
        }
        return GlobalExceptionHandler.notFound("Funcionário não encontrado");
    }
	
}