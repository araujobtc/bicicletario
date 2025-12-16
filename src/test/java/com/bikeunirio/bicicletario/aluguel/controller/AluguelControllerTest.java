package com.bikeunirio.bicicletario.aluguel.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bikeunirio.bicicletario.aluguel.dto.AluguelRequestDTO;
import com.bikeunirio.bicicletario.aluguel.dto.DevolucaoRequestDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Devolucao;
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.service.AluguelService;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;

@ExtendWith(MockitoExtension.class)
class AluguelControllerTest {

    @InjectMocks
    private AluguelController controller;

    @Mock
    private AluguelService service;

    @Mock
    private CiclistaService ciclistaService;

    @Mock
    private EquipamentosService equipamentosService;

    // POST alugar

    @Test
    void Status200AlugarBicicleta() throws Exception {
        Long idCiclista = 10L;
        Long idTranca = 5L;

        AluguelRequestDTO request = new AluguelRequestDTO();
        request.setCiclista(idCiclista);
        request.setTrancaInicio(idTranca);

        Ciclista ciclista = new Ciclista();
        ciclista.setStatus(StatusCiclista.ATIVO.getDescricao());

        Field idField = Ciclista.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(ciclista, idCiclista);

        when(ciclistaService.readCiclista(idCiclista))
                .thenReturn(Optional.of(ciclista));

        when(service.isCiclistaComAluguelAtivo(idCiclista))
                .thenReturn(false);

        Aluguel aluguel = new Aluguel();
        when(service.alugar(idTranca, ciclista))
                .thenReturn(Optional.of(aluguel));

        ResponseEntity<Object> response = controller.alugarBicicleta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(aluguel, response.getBody());

        verify(service).alugar(idTranca, ciclista);
    }

    // devolver bicicleta

    @Test
    void deveDevolverBicicleta_CaminhoFeliz() {
        Long idBicicleta = 10L;
        Long idTranca = 5L;

        DevolucaoRequestDTO request = new DevolucaoRequestDTO();
        request.setIdBicicleta(idBicicleta);
        request.setIdTranca(idTranca);

        when(equipamentosService.isTrancaDisponivel(idTranca))
                .thenReturn(true);

        Devolucao devolucao = new Devolucao();
        when(service.devolver(idBicicleta, idTranca))
                .thenReturn(Optional.of(devolucao));

        ResponseEntity<Object> response = controller.devolverBicicleta(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(devolucao, response.getBody());

        verify(service).devolver(idBicicleta, idTranca);
    }

}
