package com.bikeunirio.bicicletario.aluguel.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;

@ExtendWith(MockitoExtension.class)
class ExternoServiceTest {

    @InjectMocks
    private ExternoService service;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void deveRealizarCobrancaComSucesso() {
        Long ciclistaId = 1L;
        double valor = 10.0;

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("id", 123L);

        when(restTemplate.postForEntity(anyString(), any(), eq(Map.class))).thenReturn(new ResponseEntity<>(responseBody, HttpStatus.OK));

        Long resultado = service.realizarCobranca(ciclistaId, valor);

        assertNotNull(resultado);
        assertEquals(123L, resultado);
    }


    @Test
    void deveEnviarEmailComSucesso() {
        when(restTemplate.postForEntity(anyString(), any(), eq(String.class))).thenReturn(new ResponseEntity<>("OK", HttpStatus.OK));

        boolean resultado = service.enviarEmail("email@test.com", "mensagem");

        assertEquals(true, resultado);

        verify(restTemplate).postForEntity(anyString(), any(), eq(String.class));
    }

    @Test
    void deveRetornarFalseQuandoCartaoForValido() {
        MeioDePagamentoDTO cartao = new MeioDePagamentoDTO();

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Void.class))).thenReturn(ResponseEntity.ok().build());

        boolean resultado = service.isCartaoInvalido(cartao);

        assertEquals(false, resultado);

        verify(restTemplate).postForEntity(
                anyString(),
                any(),
                eq(Void.class));
    }

    @Test
    void deveRetornarTrueQuandoCartaoForInvalido() {
        MeioDePagamentoDTO cartao = new MeioDePagamentoDTO();

        when(restTemplate.postForEntity(
                anyString(),
                any(),
                eq(Void.class))).thenThrow(new RuntimeException("Cartão inválido"));

        boolean resultado = service.isCartaoInvalido(cartao);

        assertEquals(true, resultado);

        verify(restTemplate).postForEntity(
                anyString(),
                any(),
                eq(Void.class));
    }


}
