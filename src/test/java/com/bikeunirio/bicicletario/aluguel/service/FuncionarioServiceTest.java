package com.bikeunirio.bicicletario.aluguel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.enums.FuncionarioExemplos;
import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;

@ExtendWith(MockitoExtension.class)
class FuncionarioServiceTest {

	@InjectMocks
	private FuncionarioService service;

	@Mock
	private FuncionarioRepository repository; // Mocka o Repository

	// GET funcionarios
	@Test
	void deveRetornarTodosOsFuncionarios() {
		when(repository.findAll()).thenReturn(Arrays.asList(FuncionarioExemplos.FUNCIONARIO));

		List<Funcionario> resultado = service.getAllFuncionarios();

		assertThat(resultado).hasSize(1);
		assertThat(resultado.get(0).getNome()).isEqualTo("Isabelle");

		verify(repository, times(1)).findAll();
	}

	// POST funcionario
	@Test
	void deveCriarFuncionarioComSucesso() {
		// Quando esse método for chamado vai devolver o mesmo objeto que foi passado
		// como argumento
		when(repository.save(any(Funcionario.class))).thenAnswer(params -> params.getArgument(0));

		// Chama o service com o DTO
		Funcionario resultado = service.createFuncionario(FuncionarioExemplos.FUNCIONARIO_DTO);

		assertThat(resultado).isNotNull();
		assertThat(resultado.getNome()).isEqualTo("Isabelle");

		// Verifica se o save foi chamado 1 vez
		verify(repository, times(1)).save(Mockito.any(Funcionario.class));
	}

	// GET funcionario
	@Test
	void deveRetornarFuncionarioQuandoExistir() {
		long id = 1L;

		when(repository.findById(id)).thenReturn(Optional.of(FuncionarioExemplos.FUNCIONARIO));

		Optional<Funcionario> resultado = service.readFuncionario(id);

		assertThat(resultado).isPresent();
		assertThat(resultado.get().getNome()).isEqualTo("Isabelle");
		assertThat(resultado.get().getEmail()).isEqualTo("isa@exemplo.com");
	}

	@Test
	void deveRetornarVazioQuandoFuncionarioNaoExistir() {
		long id = 2L;
		when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<Funcionario> resultado = service.readFuncionario(id);

		assertThat(resultado).isEmpty();
	}

	// PUT funcionario
	@Test
	void deveAtualizarFuncionarioQuandoExistir() {
		long id = 1L;

		when(repository.findById(id)).thenReturn(Optional.of(FuncionarioExemplos.FUNCIONARIO));
		when(repository.save(any(Funcionario.class))).thenAnswer(invocation -> invocation.getArgument(0));

		Optional<Funcionario> resultado = service.updateFuncionario(id, FuncionarioExemplos.FUNCIONARIO_DTO);

		assertThat(resultado).isPresent();
		assertThat(resultado.get().getNome()).isEqualTo("Isabelle");
		assertThat(resultado.get().getEmail()).isEqualTo("isa@exemplo.com");
		assertThat(resultado.get().getCpf()).isEqualTo("12345678901");

		// Verifica se save foi chamado
		verify(repository, times(1)).save(any(Funcionario.class));
	}

	@Test
	void deveRetornarEmptyQuandoFuncionarioNaoExistir() {
		long id = 2L;

		when(repository.findById(id)).thenReturn(Optional.empty());

		Optional<Funcionario> resultado = service.updateFuncionario(id, FuncionarioExemplos.FUNCIONARIO_DTO);

		assertThat(resultado).isNotPresent();

		// Save não deve ser chamado
		verify(repository, never()).save(any(Funcionario.class));
	}

	// DELETE funcionario
	@Test
	void deveDeletarFuncionarioQuandoExistir() {
		Long id = 1L;

		// Mocka findById para retornar o funcionário
		when(repository.findById(id)).thenReturn(Optional.of(FuncionarioExemplos.FUNCIONARIO));

		service.deleteFuncionario(id);

		verify(repository, times(1)).delete(FuncionarioExemplos.FUNCIONARIO);
	}

}