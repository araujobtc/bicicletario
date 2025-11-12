package com.bikeunirio.bicicletario.aluguel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.repository.CiclistaRepository;

@ExtendWith(MockitoExtension.class)
public class CiclistaServiceTest {

    @InjectMocks
    private CiclistaService service;
    
    @Mock
    private CiclistaRepository repository; // Mocka o Repository

    // POST ciclista
    @Test
    void deveCriarCiclistaComSucesso() {
    	// Quando esse método for chamado vai devolver o mesmo objeto que foi passado como argumento
        when(repository.save(any(Ciclista.class))).thenAnswer(params -> params.getArgument(0));

        Ciclista resultado = service.createCiclista(CiclistaExemplos.CICLISTA_DTO);

        assertNotNull(resultado);
        assertEquals("12345678901", resultado.getCpf());
        assertNotNull(resultado.getPassaporte());
        assertNotNull(resultado.getCartao());
        assertEquals(resultado, resultado.getCartao().getCiclista()); // valida relacionamento bilateral
        verify(repository, times(1)).save(any(Ciclista.class));
    }
    
    // GET ciclista
    @Test
    void deveRetornarCiclistaQuandoEncontrado() {
    	long id = 1L;
        
        // Mockito: Quando o findById for chamado com o ID, retorne um Optional Presente
        when(repository.findById(id)).thenReturn(Optional.of(CiclistaExemplos.CICLISTA));

        Optional<Ciclista> resultado = service.readCiclista(id);

        assertTrue(resultado.isPresent(), "O resultado deve ser um Optional Presente.");
        
        verify(repository, times(1)).findById(id);
    }

    @Test
    void deveRetornarOptionalVazioQuandoCiclistaNaoExiste() {
    	long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Ciclista> resultado = service.readCiclista(id);

        assertFalse(resultado.isPresent(), "O resultado deve ser um Optional Vazio.");
        
        verify(repository, times(1)).findById(id);
    }
    
    // PUT ciclista
    @Test
    void deveAtualizarCiclistaQuandoExistir() {
        long id = 1L;

        when(repository.findById(id)).thenReturn(Optional.of(CiclistaExemplos.CICLISTA));
        when(repository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Ciclista> resultado = service.updateCiclista(id, CiclistaExemplos.CICLISTA_DTO);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("Isabelle Araujo");
        assertThat(resultado.get().getEmail()).isEqualTo("isa@exemplo.com");
        assertThat(resultado.get().getCpf()).isEqualTo("12345678901");
        assertThat(resultado.get().getNacionalidade()).isEqualTo("Brasileira");
        assertThat(resultado.get().getUrlFotoDocumento()).isEqualTo("http://exemplo.com/doc.jpg");

        // Verifica se save foi chamado
        verify(repository, times(1)).save(any(Ciclista.class));
    }

    @Test
    void deveRetornarEmptyQuandoCiclistaNaoExistir() {
        long id = 2L;

        when(repository.findById(id)).thenReturn(Optional.empty());

        Optional<Ciclista> resultado = service.updateCiclista(id, CiclistaExemplos.CICLISTA_DTO);

        assertThat(resultado).isNotPresent();

        // Save não deve ser chamado
        verify(repository, never()).save(any(Ciclista.class));
    }
    
}
