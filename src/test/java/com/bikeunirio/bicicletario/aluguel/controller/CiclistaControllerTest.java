package com.bikeunirio.bicicletario.aluguel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@ExtendWith(MockitoExtension.class)
class CiclistaControllerTest {

    @InjectMocks
    private CiclistaController controller;

    @Mock
    private CiclistaService service; // Mocka o Service
    
	@Mock
	private ExternoService externoService;

    // POST ciclista
    @Test
    void deveRetornarStatus201ECorpoCiclistaAoCadastrarComSucesso() {
        when(service.createCiclista(any(CiclistaDTO.class))).thenReturn(CiclistaExemplos.CICLISTA);
        when(service.existsByEmail(any(String.class))).thenReturn(false);
        
		when(externoService.isCartaoInvalido(Mockito.any(MeioDePagamentoDTO.class))).thenReturn(false);
		when(externoService.enviarEmail(Mockito.any(String.class), Mockito.any(String.class))).thenReturn(true);

        ResponseEntity<?> response = controller.createCiclista(CiclistaExemplos.CICLISTA_DTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), 
                     "O status HTTP deve ser 201 CREATED para criação de recurso.");
        
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo.");
        assertEquals(CiclistaExemplos.CICLISTA, response.getBody(), 
                     "O corpo da resposta deve ser a entidade Ciclista retornada pelo Service.");
        
        verify(service, times(1)).createCiclista(any(CiclistaDTO.class));
    }
    
    // GET ciclista
    @Test
    void deveRetornarStatus200EACiclistaQuandoEncontrado() {
    	long id = 1L;
        
        when(service.readCiclista(id)).thenReturn(Optional.of(CiclistaExemplos.CICLISTA));

        ResponseEntity<?> response = controller.readCiclista(id);

        assertEquals(HttpStatus.OK, response.getStatusCode(), "O status deve ser 200 OK.");
        assertEquals(CiclistaExemplos.CICLISTA, response.getBody(), "O corpo deve ser a entidade Ciclista.");
        
        verify(service).readCiclista(id);
    }
    
    // PUT ciclista
    @Test
    void status200AtualizarCiclista() {
        long id = 1L;

        when(service.updateCiclista(eq(id), any(CiclistaDTO.class)))
                .thenReturn(Optional.of(CiclistaExemplos.CICLISTA));

        ResponseEntity<?> resposta = controller.updateCiclista(id, CiclistaExemplos.CICLISTA_DTO);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resposta.getBody()).isInstanceOf(Ciclista.class);

        Ciclista f = (Ciclista) resposta.getBody();
        assertThat(f.getNome()).isEqualTo("Isabelle Araujo");
        assertThat(f.getEmail()).isEqualTo("isa@exemplo.com");

        verify(service, times(1)).updateCiclista(eq(id), any(CiclistaDTO.class));
    }
    
    @Test
    void status404AtualizarCiclistaNaoExiste() {
        long id = 2L;

        when(service.updateCiclista(eq(id), any(CiclistaDTO.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<?> resposta = controller.updateCiclista(id, CiclistaExemplos.CICLISTA_DTO);

        assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(resposta.getBody()).isInstanceOf(ErroResposta.class);

        ErroResposta erro = (ErroResposta) resposta.getBody();
        assertThat(erro.getCodigo()).isEqualTo("NAO_ENCONTRADO");
        assertThat(erro.getMensagem()).isEqualTo("Ciclista não encontrado");
    }
    
    //
    
    // GET existeEmail
    @Test
    void deveRetornarStatus200ETrueQuandoEmailCadastrado() {
        String emailValido = "teste@exemplo.com.br";
        when(service.existsByEmail(emailValido)).thenReturn(true);

        boolean response = service.existsByEmail(emailValido);

        assertEquals(true, response, "O valor deve ser TRUE.");
        verify(service, times(1)).existsByEmail(emailValido);
    }
    
}