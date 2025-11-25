package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.repository.CartaoRepository;

@ExtendWith(MockitoExtension.class)
class CartaoServiceTest {

    @InjectMocks
    private CartaoService service;
    
    @Mock
    private CartaoRepository repository; // Mocka o Repository
    
    // PUT cartao
    @Test
    void deveAtualizarCartaoExistenteESalvarComSucesso() {
    	long idCiclista = 1L;
        
        when(repository.findByCiclistaId(idCiclista)).thenReturn(Optional.of(CiclistaExemplos.CARTAO));
        when(repository.save(any(Cartao.class))).thenAnswer(params -> params.getArgument(0));

        Optional<Cartao> resultado = service.updateCartao(idCiclista, CiclistaExemplos.MEIOPAGAMENTO_DTO);

        assertTrue(resultado.isPresent(), "O resultado deve ser Optional.of(Cartao).");
        
        Cartao cartaoAtualizado = resultado.get();

        assertEquals(CiclistaExemplos.MEIOPAGAMENTO_DTO.getNumero(), cartaoAtualizado.getNumero(), 
                     "O número do cartão deve ser o novo valor do DTO.");
        assertEquals(CiclistaExemplos.MEIOPAGAMENTO_DTO.getCvv(), cartaoAtualizado.getCvv(), 
                "O cvv do cartão deve ser o novo valor do DTO.");
        assertEquals(CiclistaExemplos.MEIOPAGAMENTO_DTO.getValidade(), cartaoAtualizado.getValidade(), 
                "A validade do cartão deve ser o novo valor do DTO.");
        
        verify(repository, times(1)).save(any(Cartao.class));
        verify(repository, times(1)).findByCiclistaId(idCiclista);
    }
    
    @Test
    void deveRetornarOptionalVazioQuandoCartaoNaoExiste() {
    	long idCiclista = 1L;
        
        when(repository.findByCiclistaId(idCiclista)).thenReturn(Optional.empty());

        Optional<Cartao> resultado = service.updateCartao(idCiclista, CiclistaExemplos.MEIOPAGAMENTO_DTO);

        assertFalse(resultado.isPresent(), "O resultado deve ser Optional.empty().");
        
        verify(repository, times(1)).findByCiclistaId(idCiclista);
        verify(repository, times(0)).save(any(Cartao.class));
    }
    
    // GET cartao
    
    @Test
    void deveRetornarDTOQuandoCartaoEncontrado() {
        long idCiclista = 1L;

        Cartao cartao = CiclistaExemplos.CARTAO;

        when(repository.findByCiclistaId(idCiclista))
                .thenReturn(Optional.of(cartao));

        Optional<MeioDePagamentoDTO> resultado = service.getCartao(idCiclista);

        assertTrue(resultado.isPresent());

        MeioDePagamentoDTO dto = resultado.get();

        assertEquals(cartao.getNomeTitular(), dto.getNomeTitular());
        assertEquals(cartao.getNumero(), dto.getNumero());
        assertEquals(cartao.getValidade(), dto.getValidade());
        assertEquals(cartao.getCvv(), dto.getCvv());
    }

}