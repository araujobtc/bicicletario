package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.Nacionalidades;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ciclista")
public class CiclistaController {

	@Autowired
	private CiclistaService ciclistaService;

	@Autowired
	private ExternoService externoService;

	// UC01
	@PostMapping
	public ResponseEntity<?> createCiclista(@Valid @RequestBody CiclistaDTO ciclistaDTO) {
		if (ciclistaService.existsByEmail(ciclistaDTO.getEmail())) {
			return GlobalExceptionHandler.unprocessableEntity("Email já cadastrado!");
		}

		boolean isBrasileiro = ciclistaDTO.getNacionalidade().equals(Nacionalidades.BRASILEIRO);

		if (isBrasileiro && ciclistaDTO.getCpf().equals(null)) {
			return GlobalExceptionHandler.unprocessableEntity("Para ciclistas brasileiros, o campo CPF é obrigatório.");
		}

		if (!isBrasileiro && ciclistaDTO.getPassaporte().equals(null)) {
			return GlobalExceptionHandler
					.unprocessableEntity("Para ciclistas estrangeiros, os dados do Passaporte são obrigatórios.");
		}
		
		if (ciclistaDTO.getMeioDePagamento().equals(null)) {
			return GlobalExceptionHandler
					.unprocessableEntity("É necessário informar os dados do meio de pagamento.");
		}

		// TODO: alterar na prox entrega
		if (externoService.isCartaoInvalido(ciclistaDTO.getMeioDePagamento())) {
			return GlobalExceptionHandler.unprocessableEntity("Cartão de crédito inválido");
		}

		Ciclista ciclista = ciclistaService.createCiclista(ciclistaDTO);

		// TODO: alterar na prox entrega
		boolean isEmailEnviado = externoService.enviarEmail(ciclista.getEmail(), "cadastrado eeeeeeh!!!");
		if (!isEmailEnviado) {
			String mensagem = "Ciclista cadastrado com sucesso, mas não foi possível enviar o e-mail de confirmação.";
			return GlobalExceptionHandler.createdWithWarning(ciclista, mensagem, HttpStatus.CREATED); // 201
		}

		// Criamos os cabeçalhos e adicionamos mensagem de sucesso para o cliente ler
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Success-Message", "Email de confirmação enviado");

		return new ResponseEntity<>(ciclista, headers, HttpStatus.CREATED);
	}

	@GetMapping("/{idCiclista}")
	public ResponseEntity<?> readCiclista(@PathVariable Long idCiclista) {
		Optional<Ciclista> ciclista = ciclistaService.readCiclista(idCiclista);

		if (ciclista.isPresent()) {
			return ResponseEntity.ok(ciclista.get()); // 200
		} else {
			return GlobalExceptionHandler.notFound("Ciclista não encontrado");
		}

	}

	// UC06
	// swagger n pede senha, UC06 pede senha
	// add senha e confirmacao no ciclista
	@PutMapping("/{idCiclista}")
	public ResponseEntity<?> updateCiclista(@PathVariable Long idCiclista, @RequestBody @Valid CiclistaDTO ciclistaDTO) {
		Optional<Ciclista> atualizado = ciclistaService.updateCiclista(idCiclista, ciclistaDTO);

		if (!atualizado.isPresent()) {
			return GlobalExceptionHandler.notFound("Ciclista não encontrado");
		}
		
		Ciclista ciclista = atualizado.get();
		
		// TODO: alterar na prox entrega
		boolean isEmailEnviado = externoService.enviarEmail(ciclista.getEmail(), "atualizado eeeeeeh!!!");
		if (!isEmailEnviado) {
			String mensagem = "Cadastrado atualizado com sucesso, mas não foi possível enviar o e-mail de confirmação.";
			return GlobalExceptionHandler.createdWithWarning(ciclista, mensagem, HttpStatus.OK);
		}
		return ResponseEntity.ok(ciclista);
	}

	// UC02 @PostMapping("/{idCiclista}/ativar")public void ativar() {}

	//@GetMapping("/{idCiclista}/permiteAluguel") public void ciclistaTemPermissao() {}

	//@GetMapping("/{idCiclista}")public void getBicicletaAlugada() {}

	@GetMapping("/existeEmail/{email}")
	public ResponseEntity<?> isEmailCadastrado(@PathVariable String email) {
		String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

		if (email == null || email.isEmpty() || email.isBlank()) {
			return GlobalExceptionHandler.badRequest("O e-mail deve ser fornecido como parâmetro.");
		}

		if (!email.matches(EMAIL_REGEX)) {
			return GlobalExceptionHandler.unprocessableEntity("E-mail com formato inválido.");
		}

		return ResponseEntity.ok(ciclistaService.existsByEmail(email));
	}
}
