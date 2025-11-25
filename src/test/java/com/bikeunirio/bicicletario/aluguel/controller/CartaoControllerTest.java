package com.bikeunirio.bicicletario.aluguel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.service.CartaoService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@ExtendWith(MockitoExtension.class)
class CartaoControllerTest {

	@InjectMocks
	private CartaoController controller;

	@Mock
	private CartaoService service;

	@Mock
	private ExternoService externoService;

	// PUT cartao

	@Test
	void status200AtualizarCartao() {

		Long idCiclista = 1L;

		MeioDePagamentoDTO dto = CiclistaExemplos.MEIOPAGAMENTO_DTO;

		Mockito.when(externoService.isCartaoInvalido(dto)).thenReturn(false);
		Mockito.when(service.updateCartao(idCiclista, dto)).thenReturn(Optional.of(CiclistaExemplos.CARTAO));
		Mockito.when(externoService.enviarEmail(CiclistaExemplos.CICLISTA.getEmail(), "cartao atualizado eeeeeeh!!!")).thenReturn(true);

		ResponseEntity<Object> response = controller.updateCartao(idCiclista, dto);

		assertEquals(200, response.getStatusCode().value());
		assertEquals("Dados atualizados", response.getBody());
	}

	// GET cartao
	@Test
	void status200GetCartao() {
		long idCiclista = 1L;
		MeioDePagamentoDTO meioPagamentoDTO = CiclistaExemplos.MEIOPAGAMENTO_DTO;

		when(service.getCartao(idCiclista)).thenReturn(Optional.of(meioPagamentoDTO));

		ResponseEntity<Object> response = controller.readCartao(idCiclista);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertNotNull(response.getBody());
		assertTrue(response.getBody() instanceof MeioDePagamentoDTO);
		assertEquals(meioPagamentoDTO, response.getBody());

		verify(service, times(1)).getCartao(idCiclista);
	}

	@Test
	void Status404GetCartao() {
		long idCiclista = 2L;
		when(service.getCartao(idCiclista)).thenReturn(Optional.empty());

		ResponseEntity<Object> response = controller.readCartao(idCiclista);

		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertTrue(response.getBody() instanceof ErroResposta);

		verify(service, times(1)).getCartao(idCiclista);
	}
}