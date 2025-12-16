package com.bikeunirio.bicicletario.aluguel.webservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
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
import org.springframework.web.client.RestTemplate;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;

@ExtendWith(MockitoExtension.class)
class EquipamentosServiceTest {

    @InjectMocks
    private EquipamentosService service;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void deveRetornarBicicletaQuandoEncontrada() {
        BicicletaDTO bicicleta = new BicicletaDTO();
        bicicleta.setId(10L);

        when(restTemplate.getForEntity(
                anyString(),
                eq(BicicletaDTO.class))).thenReturn(new ResponseEntity<>(bicicleta, HttpStatus.OK));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorId(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
    }

    @Test
    void deveRetornarVazioQuandoBicicletaNaoEncontrada() {
        when(restTemplate.getForEntity(
                anyString(),
                eq(BicicletaDTO.class))).thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarStatusBicicleta() {
        service.atualizarStatusBicicleta(1L, "DISPONIVEL");

        verify(restTemplate).postForEntity(anyString(), isNull(), eq(Void.class));
    }


    @Test
    void deveAtualizarStatusTranca() {
        service.atualizarStatusTranca(5L);

        verify(restTemplate).postForEntity(anyString(), isNull(), eq(Void.class));
    }


    @Test
    void deveRetornarBicicletaPorTranca() {
        BicicletaDTO bicicleta = new BicicletaDTO();
        bicicleta.setId(7L);

        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class))).thenReturn(ResponseEntity.ok(bicicleta));

        Optional<Long> resultado = service.getBicicletaPorIdTranca(3L);

        assertEquals(true, resultado.isPresent());
        assertEquals(7L, resultado.get());
    }


}
