package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@ExtendWith(MockitoExtension.class)
class ExternoServiceTest {

    @InjectMocks
    private ExternoService externoService; 

    private MeioDePagamentoDTO createMeioDePagamentoDTO(String nomeTitular) {
        MeioDePagamentoDTO dto = new MeioDePagamentoDTO(); 
        dto.setNomeTitular(nomeTitular);
        return dto;
    }

    @Test
    void deveSempreRetornarTrueAoEnviarEmail( ) {
        String email = "teste@exemplo.com";
        String conteudo = "Teste de conteúdo";
        boolean resultado = externoService.enviarEmail(email, conteudo);

        assertTrue(resultado, "A simulação de envio de e-mail deve retornar TRUE (sucesso).");
    }

    @Test
    void deveRetornarFalseParaCartaoValidoPadrao() {
        MeioDePagamentoDTO cartaoValido = createMeioDePagamentoDTO("Titular Padrao"); 
        boolean resultado = externoService.isCartaoInvalido(cartaoValido);

        assertFalse(resultado, "Deve retornar FALSE, pois o titular é considerado válido na simulação.");
    }

}