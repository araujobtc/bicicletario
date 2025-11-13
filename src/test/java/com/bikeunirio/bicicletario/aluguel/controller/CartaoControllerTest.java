package com.bikeunirio.bicicletario.aluguel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.service.CartaoService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@ExtendWith(MockitoExtension.class)
public class CartaoControllerTest {

    @InjectMocks
    private CartaoController controller;

    @Mock
    private CartaoService service;

    @Mock
    private ExternoService externoService;
    
    // PUT cartao
    @Test
    void deveRetornar200QuandoCartaoAtualizadoEEmailEnviado() {
    	long idCiclista = 1L;
        when(externoService.isCartaoInvalido(CiclistaExemplos.MEIOPAGAMENTO_DTO)).thenReturn(false);
        when(service.updateCartao(anyLong(), any(MeioDePagamentoDTO.class))).thenReturn(Optional.of(CiclistaExemplos.CARTAO));
        when(externoService.enviarEmail(anyString(), anyString())).thenReturn(true); // Email enviado com sucesso

        ResponseEntity<?> response = controller.updateCartao(idCiclista, CiclistaExemplos.MEIOPAGAMENTO_DTO);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Deve retornar 200 OK.");
        assertEquals(CiclistaExemplos.CARTAO, response.getBody(), "O corpo deve ser o objeto Cartao atualizado.");
        
        verify(externoService, times(1)).enviarEmail(CiclistaExemplos.CARTAO.getCiclista().getEmail(), "cartao atualizado eeeeeeh!!!");
    }
	
}