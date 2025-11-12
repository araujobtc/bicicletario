package com.bikeunirio.bicicletario.aluguel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
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

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;

@ExtendWith(MockitoExtension.class)
public class CiclistaControllerTest {

    @InjectMocks
    private CiclistaController controller;

    @Mock
    private CiclistaService service; // Mocka o Service

    // POST ciclista
    @Test
    void deveRetornarStatus201ECorpoCiclistaAoCadastrarComSucesso() {
        
        Ciclista ciclistaSalvo = new Ciclista();
        ciclistaSalvo.setNome(CiclistaExemplos.CICLISTA_DTO.getNome()); 
        
        when(service.createCiclista(any(CiclistaDTO.class))).thenReturn(ciclistaSalvo);

        ResponseEntity<Ciclista> response = controller.createCiclista(CiclistaExemplos.CICLISTA_DTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode(), 
                     "O status HTTP deve ser 201 CREATED para criação de recurso.");
        
        assertNotNull(response.getBody(), "O corpo da resposta não deve ser nulo.");
        assertEquals(ciclistaSalvo, response.getBody(), 
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
    
}
