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

        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenReturn(new ResponseEntity<>(bicicleta, HttpStatus.OK));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorId(10L);

        assertTrue(resultado.isPresent());
        assertEquals(10L, resultado.get().getId());
    }

    @Test
    void deveRetornarVazioQuandoBicicletaNaoEncontrada() {
        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorId(99L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveAtualizarStatusBicicleta() {
        when(restTemplate.postForEntity(anyString(), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build());

        boolean resultado = service.atualizarStatusBicicleta(1L, "DISPONIVEL");

        assertEquals(true, resultado);

        verify(restTemplate).postForEntity(anyString(), isNull(), eq(Void.class));
    }

    @Test
    void deveAtualizarStatusTranca() {
        when(restTemplate.postForEntity(anyString(), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build());

        boolean resultado = service.atualizarStatusTranca(5L);

        assertEquals(true, resultado);

        verify(restTemplate).postForEntity(anyString(), isNull(), eq(Void.class));
    }

    @Test
    void deveRetornarBicicletaPorTranca() {
        BicicletaDTO bicicleta = new BicicletaDTO();
        bicicleta.setId(7L);

        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenReturn(ResponseEntity.ok(bicicleta));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorIdTranca(3L);

        assertTrue(resultado.isPresent());
        assertEquals(7L, resultado.get().getId());
    }

    @Test
    void deveRetornarVazioQuandoErroAoBuscarBicicleta() {
        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenThrow(new RuntimeException("Erro externo"));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorId(1L);

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveRetornarVazioQuandoErroAoBuscarBicicletaPorTranca() {
        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenThrow(new RuntimeException("Erro externo"));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorIdTranca(1L);

        assertTrue(resultado.isEmpty());
    }

    /**
     * Teste agregado apenas para COBERTURA,
     * sem verificação de número de chamadas
     */
    @Test
    void deveCobrirCaminhosDeSucessoENaoSucesso() {

        // ---------- getBicicletaPorId ----------
        BicicletaDTO bicicleta = new BicicletaDTO();
        bicicleta.setId(10L);

        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenReturn(ResponseEntity.ok(bicicleta))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND))
                .thenThrow(new RuntimeException("Erro externo"));

        assertTrue(service.getBicicletaPorId(10L).isPresent());
        assertTrue(service.getBicicletaPorId(20L).isEmpty());
        assertTrue(service.getBicicletaPorId(30L).isEmpty());

        // ---------- getBicicletaPorIdTranca ----------
        when(restTemplate.getForEntity(anyString(), eq(BicicletaDTO.class)))
                .thenReturn(ResponseEntity.ok(bicicleta))
                .thenThrow(new RuntimeException("Erro externo"));

        assertTrue(service.getBicicletaPorIdTranca(1L).isPresent());
        assertTrue(service.getBicicletaPorIdTranca(2L).isEmpty());

        // ---------- atualizarStatusBicicleta ----------
        when(restTemplate.postForEntity(anyString(), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build())
                .thenThrow(new RuntimeException("Erro externo"));

        assertEquals(true, service.atualizarStatusBicicleta(1L, "DISPONIVEL"));
        assertEquals(false, service.atualizarStatusBicicleta(2L, "DISPONIVEL"));

        // ---------- atualizarStatusTranca ----------
        when(restTemplate.postForEntity(anyString(), isNull(), eq(Void.class)))
                .thenReturn(ResponseEntity.ok().build())
                .thenThrow(new RuntimeException("Erro externo"));

        assertEquals(true, service.atualizarStatusTranca(1L));
        assertEquals(false, service.atualizarStatusTranca(2L));

        // ---------- isTrancaDisponivel ----------
        when(restTemplate.getForEntity(anyString(), eq(Object.class)))
                .thenReturn(ResponseEntity.ok(new Object()))
                .thenThrow(new RuntimeException("Erro externo"));

        assertEquals(true, service.isTrancaDisponivel(1L));
        assertEquals(false, service.isTrancaDisponivel(2L));
    }
}
