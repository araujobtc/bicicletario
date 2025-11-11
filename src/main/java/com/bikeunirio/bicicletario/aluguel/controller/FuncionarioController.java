package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

import jakarta.validation.Valid;

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
	public ResponseEntity<Funcionario> createFuncionario(@Valid @RequestBody Funcionario funcionario) {
		Funcionario novoFuncionario = funcionarioService.createFuncionario(funcionario);
		return ResponseEntity.ok(novoFuncionario);
	}

	@GetMapping("/{idFuncionario}")
	public ResponseEntity<?> readFuncionario(@PathVariable Long idFuncionario) {
		Optional<Funcionario> funcionario = funcionarioService.readFuncionario(idFuncionario);

		if (funcionario.isPresent()) {
			return ResponseEntity.ok(funcionario.get()); // 200
		} else {
			ErroResposta erro = new ErroResposta("NAO_ENCONTRADO", "Funcionário não encontrado");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro); // 404
		}
	}

	@PutMapping("/{idFuncionario}")
	public void updateFuncionario() {

	}

	@DeleteMapping("/{idFuncionario}")
	public void deleteFuncionario() {

	}

}