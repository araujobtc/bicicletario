package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Devolucao;
import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;
import com.bikeunirio.bicicletario.aluguel.repository.DevolucaoRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

    @InjectMocks
    private AluguelService service;

    @Mock
    private AluguelRepository aluguelRepository;

    @Mock
    private DevolucaoRepository devolucaoRepository;

    @Mock
    private CiclistaService ciclistaService;

    @Mock
    private EquipamentosService equipamentosService;

    @Mock
    private ExternoService externoService;

    @Test
    void deveRetornarVazioQuandoNaoExisteAluguelAtivo() {
        when(aluguelRepository.findByCiclistaIdAndHoraFimIsNull(10L))
                .thenReturn(Optional.empty());

        Optional<BicicletaDTO> resultado = service.getBicicletaPorIdCiclista(10L);

        assertTrue(resultado.isEmpty());
        verify(equipamentosService, never()).getBicicletaPorId(any());
    }

    @Test
    void deveRetornarBicicletaQuandoAluguelAtivoExiste() {
        Aluguel aluguel = new Aluguel();
        aluguel.setBicicletaId(5L);

        BicicletaDTO bicicleta = new BicicletaDTO();
        bicicleta.setId(5L);

        when(aluguelRepository.findByCiclistaIdAndHoraFimIsNull(20L))
                .thenReturn(Optional.of(aluguel));
        when(equipamentosService.getBicicletaPorId(5L))
                .thenReturn(Optional.of(bicicleta));

        Optional<BicicletaDTO> resultado = service.getBicicletaPorIdCiclista(20L);

        assertTrue(resultado.isPresent());
        assertEquals(5L, resultado.get().getId());
    }

    @Test
    void deveRetornarTrueQuandoExisteAluguelAtivo() {
        when(aluguelRepository.findByCiclistaIdAndHoraFimIsNull(1L))
                .thenReturn(Optional.of(new Aluguel()));

        assertTrue(service.isCiclistaComAluguelAtivo(1L));
    }

    @Test
    void deveAlugarBicicletaComSucesso() {
        long idTranca = 5L;
        long idBicicleta = 99L;
        long idCobranca = 123L;
        long idCiclista = 1L;

        Ciclista ciclista = criarCiclista(idCiclista, "email@test.com");

        BicicletaDTO bicicleta = new BicicletaDTO();
        bicicleta.setId(idBicicleta);

        when(equipamentosService.getBicicletaPorIdTranca(idTranca)).thenReturn(Optional.of(bicicleta));
        when(externoService.realizarCobranca(idCiclista, 10.0)).thenReturn(idCobranca);
        when(aluguelRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Optional<Aluguel> resultado = service.alugar(idTranca, ciclista);

        assertTrue(resultado.isPresent());

        Aluguel aluguel = resultado.get();
        assertEquals(idBicicleta, aluguel.getBicicletaId());
        assertEquals(idTranca, aluguel.getTrancaInicio());
        assertEquals(idCobranca, aluguel.getCobranca());
        assertNotNull(aluguel.getHoraInicio());

        verify(equipamentosService).atualizarStatusBicicleta(bicicleta.getId(), "EM_USO");
        verify(equipamentosService).atualizarStatusTranca(5L);
        verify(externoService).enviarEmail(eq("email@test.com"), anyString());
    }

    @Test
    void naoDeveAlugarQuandoNaoExisteBicicletaNaTranca() {
        when(equipamentosService.getBicicletaPorIdTranca(5L))
                .thenReturn(Optional.empty());

        Optional<Aluguel> resultado = service.alugar(5L, criarCiclista(1L, "a@a.com"));

        assertTrue(resultado.isEmpty());
        verify(aluguelRepository, never()).save(any());
    }

    @Test
    void deveDevolverBicicletaSemCobrancaExtra() {
        Ciclista ciclista = criarCiclista(1L, "email@test.com");

        Aluguel aluguel = new Aluguel();
        aluguel.setBicicletaId(1L);
        aluguel.setHoraInicio(LocalDateTime.now().minusMinutes(100));
        aluguel.setCiclista(ciclista);

        when(aluguelRepository.findByBicicletaIdAndHoraFimIsNull(1L))
                .thenReturn(Optional.of(aluguel));
        when(aluguelRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(devolucaoRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(ciclistaService.readCiclista(1L))
                .thenReturn(Optional.of(ciclista));

        Optional<Devolucao> resultado = service.devolver(1L, 2L);

        assertTrue(resultado.isPresent());
        assertNull(resultado.get().getCobranca());

        verify(equipamentosService)
                .atualizarStatusBicicleta(1L, "DISPONIVEL");
        verify(equipamentosService)
                .atualizarStatusTranca(2L);
    }

    @Test
    void deveDevolverBicicletaComCobrancaExtra() {
        Ciclista ciclista = criarCiclista(1L, "email@test.com");

        Aluguel aluguel = new Aluguel();
        aluguel.setBicicletaId(1L);
        aluguel.setHoraInicio(LocalDateTime.now().minusMinutes(200));
        aluguel.setCiclista(ciclista);

        when(aluguelRepository.findByBicicletaIdAndHoraFimIsNull(1L))
                .thenReturn(Optional.of(aluguel));
        when(externoService.realizarCobranca(1L, 10.0))
                .thenReturn(999L);
        when(aluguelRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(devolucaoRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));
        when(ciclistaService.readCiclista(1L))
                .thenReturn(Optional.of(ciclista));

        Optional<Devolucao> resultado = service.devolver(1L, 2L);

        assertTrue(resultado.isPresent());
        assertEquals(999L, resultado.get().getCobranca());

        verify(externoService)
                .realizarCobranca(1L, 10.0);
        verify(externoService)
                .enviarEmail(eq("email@test.com"), anyString());
    }

    private Ciclista criarCiclista(Long id, String email) {
        Ciclista c = new Ciclista();
        c.setEmail(email);
        try {
            Field f = Ciclista.class.getDeclaredField("id");
            f.setAccessible(true);
            f.set(c, id);
        } catch (Exception e) {
            fail("Erro ao setar id");
        }
        return c;
    }
}
