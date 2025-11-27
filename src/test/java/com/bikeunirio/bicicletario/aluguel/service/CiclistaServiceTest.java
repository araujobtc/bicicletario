package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.CiclistaExemplos;
import com.bikeunirio.bicicletario.aluguel.enums.Nacionalidades;
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.repository.CiclistaRepository;

@ExtendWith(MockitoExtension.class)
class CiclistaServiceTest {

	@InjectMocks
	private CiclistaService service;

	@Mock
	private CiclistaRepository repository; // Mocka o Repository

	@Mock
	private AluguelService aluguelService;

	// POST ciclista
	@Test
	void deveCriarCiclistaComSucesso() {
		// Quando esse método for chamado vai devolver o mesmo objeto que foi passado
		// como argumento
		when(repository.save(any(Ciclista.class))).thenAnswer(params -> params.getArgument(0));

		Ciclista resultado = service.createCiclista(CiclistaExemplos.CICLISTA_DTO);

		assertNotNull(resultado);
		assertEquals("12345678901", resultado.getCpf());
		assertNotNull(resultado.getPassaporte());
		assertNotNull(resultado.getCartao());
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
		when(repository.save(any(Ciclista.class))).thenAnswer(params -> params.getArgument(0));

		Optional<Ciclista> resultado = service.updateCiclista(id, CiclistaExemplos.CICLISTA_DTO);

		assertTrue(resultado.isPresent());
		assertEquals("Isabelle Araujo", resultado.get().getNome());
		assertEquals("isa@exemplo.com", resultado.get().getEmail());
		assertEquals("12345678901", resultado.get().getCpf());
		assertEquals(Nacionalidades.BRASILEIRO.getValor(), resultado.get().getNacionalidade());
		assertEquals("http://exemplo.com/doc.jpg", resultado.get().getUrlFotoDocumento());

		verify(repository, times(1)).save(any(Ciclista.class));
	}

	@Test
	void deveRetornarEmptyQuandoCiclistaNaoExistir() {
		long id = 2L;

		when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<Ciclista> resultado = service.updateCiclista(id, CiclistaExemplos.CICLISTA_DTO);

		assertFalse(resultado.isPresent());

		verify(repository, never()).save(any(Ciclista.class));
	}

	// POST ativar

	@Test
	void deveAtivarCadastroCiclista_QuandoCiclistaExiste() {
		Long idCiclista = 1L;

		Ciclista ciclista = new Ciclista();
		ciclista.setStatus(StatusCiclista.CADASTRADO.getDescricao());

		try {
			Field idField = Ciclista.class.getDeclaredField("id");
			idField.setAccessible(true);
			idField.set(ciclista, idCiclista);
		} catch (Exception e) {
			fail("Falha ao setar o ID via reflection");
		}

		when(repository.findById(idCiclista)).thenReturn(Optional.of(ciclista));
		when(repository.save(any(Ciclista.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Optional<Ciclista> resultado = service.ativarCadastroCiclista(idCiclista);

		assertTrue(resultado.isPresent());
		assertEquals(StatusCiclista.ATIVO.getDescricao(), resultado.get().getStatus());
		assertNotNull(resultado.get().getDataConfirmacaoEmail());

		verify(repository).findById(idCiclista);
		verify(repository).save(ciclista);
	}

	// aluguel ativo

	@Test
	void deveRetornarTrueSeCiclistaNaoTemAluguelAtivo() {
		Long idCiclista = 1L;

		// Ciclista não tem aluguel ativo
		when(aluguelService.isCiclistaComAluguelAtivo(idCiclista)).thenReturn(false);

		boolean resultado = service.temPermissaoAluguel(idCiclista);

		assertTrue(resultado);
		verify(aluguelService).isCiclistaComAluguelAtivo(idCiclista);
	}

	@Test
	void deveRetornarFalseSeCiclistaTemAluguelAtivo() {
		Long idCiclista = 1L;

		// Ciclista tem aluguel ativo
		when(aluguelService.isCiclistaComAluguelAtivo(idCiclista)).thenReturn(true);

		boolean resultado = service.temPermissaoAluguel(idCiclista);

		assertFalse(resultado);
		verify(aluguelService).isCiclistaComAluguelAtivo(idCiclista);
	}

	// bicicleta alugada

	@Test
	void deveRetornarBicicletaAlugadaSeExistir() {
		Long idCiclista = 1L;
		BicicletaDTO bicicletaDTO = new BicicletaDTO();
		bicicletaDTO.setId(10L);

		when(aluguelService.getBicicletaPorIdCiclista(idCiclista)).thenReturn(Optional.of(bicicletaDTO));

		Optional<BicicletaDTO> resultado = service.getBicicletaAlugada(idCiclista);

		assertTrue(resultado.isPresent());
		assertEquals(bicicletaDTO, resultado.get());
		verify(aluguelService).getBicicletaPorIdCiclista(idCiclista);
	}

	@Test
	void deveRetornarOptionalVazioSeNaoExistirBicicletaAlugada() {
		Long idCiclista = 1L;

		when(aluguelService.getBicicletaPorIdCiclista(idCiclista)).thenReturn(Optional.empty());

		Optional<BicicletaDTO> resultado = service.getBicicletaAlugada(idCiclista);

		assertTrue(resultado.isEmpty());
		verify(aluguelService).getBicicletaPorIdCiclista(idCiclista);
	}

	// codes

	@Test
	void deveValidarCodigoCorretoDoCiclista() throws Exception {
		Ciclista ciclista = new Ciclista();

		Field idField = Ciclista.class.getDeclaredField("id");
		idField.setAccessible(true);
		idField.set(ciclista, 1L);

		ciclista.setCpf("12345678901");
		ciclista.setNascimento(LocalDate.of(1990, 5, 20));

		Long codigoGerado = Long.parseLong(service.gerarCodigo(ciclista).toString());

		assertTrue(service.isCodigoValido(codigoGerado, ciclista));
		assertFalse(service.isCodigoValido(codigoGerado + 1, ciclista));

		assertFalse(service.isCodigoValido(null, ciclista));
	}

	@Test
	void deveGerarCodigoParaCiclista() throws Exception {
		Ciclista ciclista = new Ciclista();

		Field idField = Ciclista.class.getDeclaredField("id");
		idField.setAccessible(true);
		idField.set(ciclista, 1L);

		ciclista.setCpf("12345678901");
		ciclista.setNascimento(LocalDate.of(1990, 5, 20));

		Integer codigo = service.gerarCodigo(ciclista);

		assertNotNull(codigo);
		assertTrue(codigo > 0);

		Integer codigo2 = service.gerarCodigo(ciclista);
		assertEquals(codigo, codigo2);
	}

}