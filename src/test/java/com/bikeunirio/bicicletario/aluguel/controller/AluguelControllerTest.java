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
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.service.AluguelService;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;

@ExtendWith(MockitoExtension.class)
public class AluguelControllerTest {

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

		when(ciclistaService.readCiclista(idCiclista)).thenReturn(Optional.of(ciclista));
		when(service.isCiclistaComAluguelAtivo(idCiclista)).thenReturn(false);

		ResponseEntity<Object> responseEsperado = ResponseEntity.ok("aluguel criado");
		when(service.alugarBicicleta(idTranca, ciclista)).thenReturn(responseEsperado);

		ResponseEntity<Object> response = controller.alugarBicicleta(request);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals("aluguel criado", response.getBody());

		verify(ciclistaService).readCiclista(idCiclista);
		verify(service).isCiclistaComAluguelAtivo(idCiclista);
		verify(service).alugarBicicleta(idTranca, ciclista);
	}

	// devolver bicicleta
	
	@Test
	void deveDevolverBicicleta_CaminhoFeliz() {
	    Long idBicicleta = 10L;
	    Long idTranca = 5L;

	    DevolucaoRequestDTO requestDTO = new DevolucaoRequestDTO();
	    requestDTO.setIdBicicleta(idBicicleta);
	    requestDTO.setIdTranca(idTranca);

	    when(equipamentosService.isTrancaDisponivel(idTranca)).thenReturn(true);

	    Aluguel aluguel = new Aluguel();
	    aluguel.setBicicletaId(idBicicleta);
	    aluguel.setTrancaFim(idTranca);

	    ResponseEntity<Object> responseEsperado = ResponseEntity.ok(aluguel);
	    when(service.devolverBicicleta(idBicicleta, idTranca)).thenReturn(responseEsperado);

	    ResponseEntity<Object> response = controller.devolverBicicleta(requestDTO);

	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    assertEquals(aluguel, response.getBody());

	    verify(equipamentosService).isTrancaDisponivel(idTranca);
	    verify(service).devolverBicicleta(idBicicleta, idTranca);
	}


}
