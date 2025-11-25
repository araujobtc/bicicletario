package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.CartaoService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cartaoDeCredito")
public class CartaoController {

	private CartaoService cartaoService;

	private ExternoService externoService;
	
	public CartaoController(CartaoService cartaoService, ExternoService externoService) {
		this.cartaoService = cartaoService;
		this.externoService = externoService;
	}

	// UC07
	@PutMapping("/{idCiclista}")
    public ResponseEntity<Object> updateCartao(@PathVariable Long idCiclista, @RequestBody @Valid MeioDePagamentoDTO meioDePagamentoDTO) {
        
		// COMENT: alterar na prox entrega
		if (externoService.isCartaoInvalido(meioDePagamentoDTO)) {
			return GlobalExceptionHandler.unprocessableEntity("Cartão de crédito inválido");
		}
		
		Optional<Cartao> resultado = cartaoService.updateCartao(idCiclista, meioDePagamentoDTO);
        
        if (resultado.isEmpty()) {
            return GlobalExceptionHandler.notFound("Ciclista ou cartão de crédito não encontrado para o ID: " + idCiclista);
        }
        
        Ciclista ciclista = resultado.get().getCiclista();
        
		// COMENT: alterar na prox entrega
		boolean isEmailEnviado = externoService.enviarEmail(ciclista.getEmail(), "cartao atualizado eeeeeeh!!!");
		if (!isEmailEnviado) {
			String mensagem = "Cartao atualizado com sucesso, mas não foi possível enviar o e-mail de confirmação.";
			return GlobalExceptionHandler.createdWithWarning(ciclista, mensagem, HttpStatus.CREATED); // 201
		}

		return ResponseEntity.ok("Dados atualizados");
    }

	@GetMapping("/{idCiclista}")
	public ResponseEntity<Object> readCartao(@PathVariable Long idCiclista){
		// optei por utilizar o dto que já tenho, como consequencia nao apresento o id
		Optional<MeioDePagamentoDTO> response = cartaoService.getCartao(idCiclista);
		
		if(response.isEmpty()) {
			return GlobalExceptionHandler.notFound("Cartão não encontrado.");
		}
		
		return ResponseEntity.ok(response.get());
	}
}
