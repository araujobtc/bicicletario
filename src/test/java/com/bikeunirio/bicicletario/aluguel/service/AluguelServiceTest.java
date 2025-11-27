package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;

@ExtendWith(MockitoExtension.class)
public class AluguelServiceTest {

	@InjectMocks
	private AluguelService service;

	@Mock
	private AluguelRepository repository;

	@Mock
	private EquipamentosService equipamentosService;

	// get bicicleta p/ idCiclista

	@Test
	void deveRetornarVazioQuandoNaoExisteAluguelAtivo() {
		Long idCiclista = 10L;

		when(repository.findByCiclistaIdAndHoraFimIsNull(idCiclista)).thenReturn(Optional.empty());

		Optional<BicicletaDTO> resultado = service.getBicicletaPorIdCiclista(idCiclista);

		assertTrue(resultado.isEmpty());
		verify(equipamentosService, never()).getBicicletaPorId(any());
	}

	@Test
	void deveRetornarBicicletaQuandoAluguelAtivoExiste() {
		Long idCiclista = 20L;

		Aluguel aluguel = new Aluguel();
		aluguel.setBicicletaId(5L);

		BicicletaDTO bicicletaDTO = new BicicletaDTO();
		bicicletaDTO.setId(5L);
		bicicletaDTO.setMarca("Caloi");
		bicicletaDTO.setModelo("Elite");

		when(repository.findByCiclistaIdAndHoraFimIsNull(idCiclista)).thenReturn(Optional.of(aluguel));
		when(equipamentosService.getBicicletaPorId(5L)).thenReturn(Optional.of(bicicletaDTO));

		Optional<BicicletaDTO> resultado = service.getBicicletaPorIdCiclista(idCiclista);

		assertTrue(resultado.isPresent());
		assertEquals(5L, resultado.get().getId());

		verify(equipamentosService).getBicicletaPorId(5L);
	}

	// algum aluguel ativo
	@Test
	void deveRetornarTrueQuandoExisteAluguelAtivo() {
		Long idCiclista = 1L;

		Aluguel aluguel = new Aluguel();
		aluguel.setId(100L);

		when(repository.findByCiclistaIdAndHoraFimIsNull(idCiclista)).thenReturn(Optional.of(aluguel));

		boolean resultado = service.isCiclistaComAluguelAtivo(idCiclista);

		assertTrue(resultado);
		verify(repository).findByCiclistaIdAndHoraFimIsNull(idCiclista);
	}

}
