package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

	@InjectMocks
	private AluguelService service;

	@Mock
	private AluguelRepository repository;

	@Mock
	private CiclistaService ciclistaService;

	@Mock
	private EquipamentosService equipamentosService;

	@Mock
	private ExternoService externoService;

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

	// alugar bicicleta
	@Test
	void deveAlugarBicicleta_comSucesso() {
		Long trancaInicio = 5L;
		Long bicicletaId = 99L;
		Long idCobranca = 123L;

		Ciclista ciclista = new Ciclista();
		ciclista.setEmail("email@test.com");
		ciclista.setStatus("Ativo");

		try {
			Field idField = Ciclista.class.getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(ciclista, 1L);
		} catch (Exception e) {
			fail("Falha ao setar o ID via reflection");
		}

		when(equipamentosService.getBicicletaPorIdTranca(trancaInicio)).thenReturn(Optional.of(bicicletaId));

		when(externoService.realizarCobranca(ciclista.getId(), 10.0)).thenReturn(idCobranca);

		when(repository.save(Mockito.any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));

		ResponseEntity<Object> response = service.alugarBicicleta(trancaInicio, ciclista);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(Aluguel.class, response.getBody().getClass());

		Aluguel aluguelSalvo = (Aluguel) response.getBody();

		assertEquals(1L, aluguelSalvo.getCiclista());
		assertEquals(bicicletaId, aluguelSalvo.getBicicletaId());
		assertEquals(trancaInicio, aluguelSalvo.getTrancaInicio());
		assertNotNull(aluguelSalvo.getHoraInicio());

		assertEquals(1, aluguelSalvo.getCobrancas().size());
		assertEquals(idCobranca, aluguelSalvo.getCobrancas().get(0));

		verify(externoService).realizarCobranca(ciclista.getId(), 10.0);
		verify(externoService).enviarEmail("email@test.com", "ALUGOU eeeeeeh!!!");
		verify(repository).save(Mockito.any(Aluguel.class));
	}

	// devolver bicicleta

	@Test
	void deveDevolverBicicleta_SemCobrancaExtra() {
		Long bicicletaId = 1L;
		Long trancaId = 2L;

		Ciclista ciclista = new Ciclista();
		ciclista.setEmail("email@test.com");
		try {
			Field idField = Ciclista.class.getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(ciclista, 1L);
		} catch (Exception e) {
			fail("Falha ao setar o ID via reflection");
		}

		Aluguel aluguel = new Aluguel();
		aluguel.setBicicletaId(bicicletaId);
		aluguel.setHoraInicio(LocalDateTime.now().minusMinutes(100));
		aluguel.setCiclista(ciclista);

		when(repository.findByBicicletaIdAndHoraFimIsNull(bicicletaId)).thenReturn(Optional.of(aluguel));
		when(repository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(ciclistaService.readCiclista(ciclista.getId())).thenReturn(Optional.of(ciclista));

		ResponseEntity<Object> response = service.devolverBicicleta(bicicletaId, trancaId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		Aluguel resultado = (Aluguel) response.getBody();
		assertNotNull(resultado.getHoraFim());
		assertEquals(trancaId, resultado.getTrancaFim());
		assertTrue(resultado.getCobrancas().isEmpty());

		verify(equipamentosService).atualizarStatusBicicleta(bicicletaId, "DISPONIVEL");
		verify(equipamentosService).atualizarStatusTranca(trancaId);
	}

	@Test
	void deveDevolverBicicleta_ComCobrancaExtra() {
		Long bicicletaId = 1L;
		Long trancaId = 2L;

		Ciclista ciclista = new Ciclista();
		ciclista.setEmail("email@test.com");
		try {
			Field idField = Ciclista.class.getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(ciclista, 1L);
		} catch (Exception e) {
			fail("Falha ao setar o ID via reflection");
		}

		Aluguel aluguel = new Aluguel();
		aluguel.setBicicletaId(bicicletaId);
		aluguel.setHoraInicio(LocalDateTime.now().minusMinutes(200));
		aluguel.setCiclista(ciclista);

		when(repository.findByBicicletaIdAndHoraFimIsNull(bicicletaId)).thenReturn(Optional.of(aluguel));
		when(externoService.cobrar(ciclista.getId(), 10.0)).thenReturn(100L);
		when(repository.save(any(Aluguel.class))).thenAnswer(invocation -> invocation.getArgument(0));
		when(ciclistaService.readCiclista(ciclista.getId())).thenReturn(Optional.of(ciclista));

		ResponseEntity<Object> response = service.devolverBicicleta(bicicletaId, trancaId);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		Aluguel resultado = (Aluguel) response.getBody();
		assertEquals(1, resultado.getCobrancas().size());

		verify(externoService).cobrar(ciclista.getId(), 10.0);
		verify(equipamentosService).atualizarStatusBicicleta(bicicletaId, "DISPONIVEL");
		verify(equipamentosService).atualizarStatusTranca(trancaId);
		verify(externoService).enviarEmail(ciclista.getEmail(), "bicicleta devolvida!");
	}

}
