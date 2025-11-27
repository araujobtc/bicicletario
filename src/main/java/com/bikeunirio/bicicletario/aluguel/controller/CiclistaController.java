package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.Nacionalidades;
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.AluguelService;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ciclista")
public class CiclistaController {

	private CiclistaService ciclistaService;

	private AluguelService aluguelService;
	
	private ExternoService externoService;

	public CiclistaController(CiclistaService ciclistaService, AluguelService aluguelService, ExternoService externoService) {
		this.ciclistaService = ciclistaService;
		this.aluguelService = aluguelService;
		this.externoService = externoService;
	}

	private String notFoundCiclista = "Ciclista não encontrado";

	// UC01
	@PostMapping
	public ResponseEntity<Object> createCiclista(@Valid @RequestBody CiclistaDTO ciclistaDTO) {
		if (ciclistaService.existsByEmail(ciclistaDTO.getEmail())) {
			return GlobalExceptionHandler.unprocessableEntity("Email já cadastrado!");
		}

		boolean isBrasileiro = ciclistaDTO.getNacionalidade().equals(Nacionalidades.BRASILEIRO.getValor());

		if (isBrasileiro && ciclistaDTO.getCpf() == null) {
			return GlobalExceptionHandler.unprocessableEntity("Para ciclistas brasileiros, o campo CPF é obrigatório.");
		}

		if (!isBrasileiro && ciclistaDTO.getPassaporte() == null) {
			return GlobalExceptionHandler
					.unprocessableEntity("Para ciclistas estrangeiros, os dados do Passaporte são obrigatórios.");
		}

		if (ciclistaDTO.getMeioDePagamento() == null) {
			return GlobalExceptionHandler.unprocessableEntity("É necessário informar os dados do meio de pagamento.");
		}

		// COMENT: alterar na prox entrega
		if (externoService.isCartaoInvalido(ciclistaDTO.getMeioDePagamento())) {
			return GlobalExceptionHandler.unprocessableEntity("Cartão de crédito inválido");
		}

		Ciclista ciclista = ciclistaService.createCiclista(ciclistaDTO);

		// COMENT: alterar na prox entrega
		boolean isEmailEnviado = externoService.enviarEmail(ciclista.getEmail(), "cadastrado eeeeeeh!!!");
		if (!isEmailEnviado) {
			// gerarCodigo
			String mensagem = "Ciclista cadastrado com sucesso, mas não foi possível enviar o e-mail de confirmação.";
			return GlobalExceptionHandler.createdWithWarning(ciclista, mensagem, HttpStatus.CREATED); // 201
		}

		// Criamos os cabeçalhos e adicionamos mensagem de sucesso para o cliente ler
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Success-Message", "Email de confirmação enviado");

		return new ResponseEntity<>(ciclista, headers, HttpStatus.CREATED);
	}

	@GetMapping("/{idCiclista}")
	public ResponseEntity<Object> readCiclista(@PathVariable Long idCiclista) {
		Optional<Ciclista> ciclista = ciclistaService.readCiclista(idCiclista);

		if (ciclista.isPresent()) {
			return ResponseEntity.ok(ciclista.get()); // 200
		} else {
			return GlobalExceptionHandler.notFound(notFoundCiclista);
		}

	}

	// UC06
	// swagger n pede senha, UC06 pede senha
	// add senha e confirmacao no ciclista
	@PutMapping("/{idCiclista}")
	public ResponseEntity<Object> updateCiclista(@PathVariable Long idCiclista,
			@RequestBody @Valid CiclistaDTO ciclistaDTO) {
		Optional<Ciclista> atualizado = ciclistaService.updateCiclista(idCiclista, ciclistaDTO);

		if (!atualizado.isPresent()) {
			return GlobalExceptionHandler.notFound(notFoundCiclista);
		}

		Ciclista ciclista = atualizado.get();

		// COMENT: alterar na prox entrega
		boolean isEmailEnviado = externoService.enviarEmail(ciclista.getEmail(), "atualizado eeeeeeh!!!");
		if (!isEmailEnviado) {
			String mensagem = "Cadastrado atualizado com sucesso, mas não foi possível enviar o e-mail de confirmação.";
			return GlobalExceptionHandler.createdWithWarning(ciclista, mensagem, HttpStatus.OK);
		}
		return ResponseEntity.ok(ciclista);
	}

	@PostMapping("/{idCiclista}/ativar")
	public ResponseEntity<Object> ativarCadastroCiclista(@PathVariable Long idCiclista,
			@RequestHeader("X-Id-Requisicao") Long idRequisicao) {
		Optional<Ciclista> response = ciclistaService.readCiclista(idCiclista);

		if (response.isEmpty()) {
			return GlobalExceptionHandler.notFound(notFoundCiclista);
		}

		Ciclista ciclista = response.get();

		if (ciclista.getStatus().equals(StatusCiclista.ATIVO.getDescricao())) {
			return GlobalExceptionHandler.unprocessableEntity("Ciclista já está ativo.");
		}

		if (!ciclistaService.isCodigoValido(idRequisicao, ciclista)) {
		    return GlobalExceptionHandler.unprocessableEntity("Código de ativação inválido para este ciclista.");
		}

		Optional<Ciclista> ciclistaAtivo =  ciclistaService.ativarCadastroCiclista(ciclista.getId());

		if (ciclistaAtivo.isEmpty()) {
		    return ResponseEntity.ok(Collections.emptyMap());
		}

		return ResponseEntity.ok(ciclistaAtivo.get());
	}

	@GetMapping("/{idCiclista}/permiteAluguel")
	public ResponseEntity<Object> temPermissaoAluguel(@PathVariable Long idCiclista) {
		if (!ciclistaService.existsById(idCiclista)) {
			return GlobalExceptionHandler.notFound(notFoundCiclista);
		}

		return ResponseEntity.ok(!aluguelService.isCiclistaComAluguelAtivo(idCiclista));
	}

	@GetMapping("/{idCiclista}/bicicletaAlugada")
	public ResponseEntity<Object> getBicicletaAlugada(@PathVariable Long idCiclista) {
		if (!ciclistaService.existsById(idCiclista)) {
			return GlobalExceptionHandler.notFound(notFoundCiclista);
		}

		Optional<BicicletaDTO> response = aluguelService.getBicicletaPorIdCiclista(idCiclista);

		if (response.isEmpty()) {
	        return ResponseEntity.ok().build();
		}

		return ResponseEntity.ok(response.get());
	}

	//

	@GetMapping("/existeEmail/{email}")
	public ResponseEntity<Object> isEmailCadastrado(@PathVariable String email) {
		String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";

		if (email == null || email.isEmpty() || email.isBlank()) {
			return GlobalExceptionHandler.badRequest("O e-mail deve ser fornecido como parâmetro.");
		}

		if (!email.matches(emailRegex)) {
			return GlobalExceptionHandler.unprocessableEntity("E-mail com formato inválido.");
		}

		return ResponseEntity.ok(ciclistaService.existsByEmail(email));
	}
}
