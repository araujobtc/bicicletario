package com.bikeunirio.bicicletario.aluguel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
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
import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.enums.Nacionalidades;
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

	private CiclistaDTO createBaseCiclistaDTO() {
	    CiclistaDTO dto = new CiclistaDTO();
	    
        dto = new CiclistaDTO();
        dto.setNome("Isabelle Araujo");
        dto.setEmail("isa@exemplo.com");
        dto.setCpf("12345678901");
        dto.setNacionalidade(Nacionalidades.BRASILEIRO);
        dto.setSenha("senha123");
        dto.setUrlFotoDocumento("foto.png");
        dto.setNascimento(java.time.LocalDate.of(2000, 1, 1));
        dto.setMeioDePagamento(new MeioDePagamentoDTO());
	    
	    return dto;
	}
    
    @Test
    void deveCobrirTodosOsCaminhosPossiveis() {
    	CiclistaDTO dto = createBaseCiclistaDTO();
    	
        when(service.existsByEmail(anyString())).thenReturn(true);
        ResponseEntity<Object> r1 = controller.createCiclista(dto);
        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        when(service.existsByEmail(anyString())).thenReturn(false);
        dto.setCpf(null);
        ResponseEntity<Object> r2 = controller.createCiclista(dto);
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        dto.setCpf(null);
        dto.setNacionalidade(Nacionalidades.ESTRANGEIRO);
        dto.setPassaporte(null);
        ResponseEntity<Object> r3 = controller.createCiclista(dto);
        assertThat(r3.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        dto.setNacionalidade(Nacionalidades.BRASILEIRO);
        dto.setCpf("12345678901");
        dto.setMeioDePagamento(null);
        ResponseEntity<Object> r4 = controller.createCiclista(dto);
        assertThat(r4.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        dto.setMeioDePagamento(new MeioDePagamentoDTO());
        when(externoService.isCartaoInvalido(any())).thenReturn(true);
        ResponseEntity<Object> r5 = controller.createCiclista(dto);
        assertThat(r5.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        Ciclista ciclista = new Ciclista();
        ciclista.setEmail("isa@exemplo.com");
        
        when(externoService.isCartaoInvalido(any())).thenReturn(false);
        when(service.createCiclista(any())).thenReturn(ciclista);
        when(externoService.enviarEmail(anyString(), anyString())).thenReturn(false);
        ResponseEntity<Object> r6 = controller.createCiclista(dto);
        assertThat(r6.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        when(externoService.enviarEmail(anyString(), anyString())).thenReturn(true);
        ResponseEntity<Object> r7 = controller.createCiclista(dto);
        assertThat(r7.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(r7.getHeaders().containsKey("X-Success-Message")).isTrue();

        verify(service, atLeastOnce()).existsByEmail(anyString());
        verify(service, atLeastOnce()).createCiclista(any());
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
    void deveCobrirTodosOsCaminhosDeIsEmailCadastrado() {
        ResponseEntity<Object> r1 = controller.isEmailCadastrado(null);
        assertThat(r1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ResponseEntity<Object> r2 = controller.isEmailCadastrado("");
        assertThat(r2.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ResponseEntity<Object> r3 = controller.isEmailCadastrado("invalido@");
        assertThat(r3.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);

        when(service.existsByEmail("isa@exemplo.com")).thenReturn(false);
        ResponseEntity<Object> r4 = controller.isEmailCadastrado("isa@exemplo.com");
        assertThat(r4.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r4.getBody()).isEqualTo(false);

        when(service.existsByEmail("ja@exemplo.com")).thenReturn(true);
        ResponseEntity<Object> r5 = controller.isEmailCadastrado("ja@exemplo.com");
        assertThat(r5.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(r5.getBody()).isEqualTo(true);

        verify(service, times(2)).existsByEmail(anyString());
    }
    
}