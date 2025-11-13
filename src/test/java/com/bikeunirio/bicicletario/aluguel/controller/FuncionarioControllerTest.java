package com.bikeunirio.bicicletario.aluguel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.enums.FuncionarioExemplos;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

@ExtendWith(MockitoExtension.class)
public class FuncionarioControllerTest {
	@InjectMocks
	private FuncionarioController controller;

	@Mock
	private FuncionarioService service; // Mocka o Service

	// GET funcionarios
	@Test
	void status200RetornarListaDeFuncionarios() {
		List<Funcionario> funcionarios = Arrays.asList(FuncionarioExemplos.FUNCIONARIO);

		when(service.getAllFuncionarios()).thenReturn(funcionarios);

		ResponseEntity<List<Funcionario>> resposta = controller.getAllFuncionarios();

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resposta.getBody()).hasSize(1);
		assertThat(resposta.getBody().get(0).getNome()).isEqualTo("Isabelle");
	}

	// POST funcionario
	@Test
	void status200CriarFuncionario() {
		// Mocka o service para retornar o funcionário criado
		when(service.createFuncionario(Mockito.any(FuncionarioDTO.class))).thenReturn(FuncionarioExemplos.FUNCIONARIO);
		// Chama o controller com o DTO válido
		ResponseEntity<?> resposta = controller.createFuncionario(FuncionarioExemplos.FUNCIONARIO_DTO);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);

		// Verifica que o corpo da resposta é um Funcionario e tem os valores corretos
		assertThat(resposta.getBody()).isInstanceOf(Funcionario.class);
		Funcionario f = (Funcionario) resposta.getBody();
		assertThat(f.getNome()).isEqualTo("Isabelle");
		assertThat(f.getEmail()).isEqualTo("isa@exemplo.com");
	}

	@Test
	void status422CriarFuncionarioDadosInvalidos() {
		// cria um funcionário inválido com todos os campos obrigatórios ausentes
		Funcionario funcionarioInvalido = new Funcionario();

		// Cria o BeanPropertyBindingResult para armazenar os erros de validação
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(funcionarioInvalido, "funcionario");

		// Adiciona erros de validação manualmente (simulando erros do @Valid)
		bindingResult.addError(new FieldError("funcionario", "nome", "O nome é obrigatório"));
		bindingResult.addError(new FieldError("funcionario", "email", "O e-mail é obrigatório"));

		// Mocka a exceção que normalmente seria lançada pelo Spring ao validar o
		// @RequestBody
		MethodArgumentNotValidException ex = org.mockito.Mockito.mock(MethodArgumentNotValidException.class);
		when(ex.getBindingResult()).thenReturn(bindingResult);

		// chama o handler que converte os erros em ResponseEntity
		ResponseEntity<List<ErroResposta>> resposta = new GlobalExceptionHandler().handleValidationExceptions(ex);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		assertThat(resposta.getBody()).isNotNull();
		assertThat(resposta.getBody()).hasSize(2);
		assertThat(resposta.getBody().get(0).getMensagem()).isEqualTo("O nome é obrigatório");
		assertThat(resposta.getBody().get(1).getMensagem()).isEqualTo("O e-mail é obrigatório");
	}

	/* GET funcionario */
	@Test
	void status200RetornarFuncionario() {
		when(service.readFuncionario(1L)).thenReturn(Optional.of(FuncionarioExemplos.FUNCIONARIO));

		ResponseEntity<?> resposta = controller.readFuncionario(1L);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resposta.getBody()).isInstanceOf(Funcionario.class);
		Funcionario f = (Funcionario) resposta.getBody();
		assertThat(f.getNome()).isEqualTo("Isabelle");
	}

	@Test
	void status404RetornarFuncionarioNaoExiste() {
		when(service.readFuncionario(2L)).thenReturn(Optional.empty());

		ResponseEntity<?> resposta = controller.readFuncionario(2L);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(resposta.getBody()).isInstanceOf(ErroResposta.class);
		ErroResposta erro = (ErroResposta) resposta.getBody();
		assertThat(erro.getCodigo()).isEqualTo("NAO_ENCONTRADO");
		assertThat(erro.getMensagem()).isEqualTo("Funcionário não encontrado");
	}

	/*
	 * No swagger é solicitado retorno de erro 422 para dados inválidos
	 */
	@Test
	void status422RetornarFuncionarioParametroInvalido() {
		// Simula exceção lançada pelo Spring quando o id não é um número válido
		MethodArgumentTypeMismatchException ex = new MethodArgumentTypeMismatchException("abc", Long.class,
				"idFuncionario", null, null);

		// Chama o handler diretamente
		ResponseEntity<ErroResposta> resposta = new GlobalExceptionHandler().handleTypeMismatch(ex);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		assertThat(resposta.getBody()).isInstanceOf(ErroResposta.class);
		assertThat(resposta.getBody().getCodigo()).isEqualTo("DADOS_INVALIDOS");
		assertThat(resposta.getBody().getMensagem()).contains("O valor do parâmetro 'idFuncionario' é inválido.");
	}

	// PUT funcionario
	@Test
	void status200AtualizarFuncionario() {
		long id = 1L;

		when(service.updateFuncionario(eq(id), any(FuncionarioDTO.class)))
				.thenReturn(Optional.of(FuncionarioExemplos.FUNCIONARIO));

		ResponseEntity<?> resposta = controller.updateFuncionario(id, FuncionarioExemplos.FUNCIONARIO_DTO);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resposta.getBody()).isInstanceOf(Funcionario.class);

		Funcionario f = (Funcionario) resposta.getBody();
		assertThat(f.getNome()).isEqualTo("Isabelle");
		assertThat(f.getEmail()).isEqualTo("isa@exemplo.com");

		verify(service, times(1)).updateFuncionario(eq(id), any(FuncionarioDTO.class));
	}

	@Test
	void status404AtualizarFuncionarioNaoExiste() {
		long id = 2L;

		when(service.updateFuncionario(eq(id), any(FuncionarioDTO.class))).thenReturn(Optional.empty());

		ResponseEntity<?> resposta = controller.updateFuncionario(id, FuncionarioExemplos.FUNCIONARIO_DTO);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(resposta.getBody()).isInstanceOf(ErroResposta.class);

		ErroResposta erro = (ErroResposta) resposta.getBody();
		assertThat(erro.getCodigo()).isEqualTo("NAO_ENCONTRADO");
		assertThat(erro.getMensagem()).isEqualTo("Funcionário não encontrado");
	}

	@Test
	void status422AtualizarFuncionarioSenhaDiferenteDaConfirmacao() {
		long id = 1L;

		FuncionarioDTO dtoComSenhaErrada = new FuncionarioDTO();
		dtoComSenhaErrada.setNome("Isabelle");
		dtoComSenhaErrada.setEmail("isa@exemplo.com");
		dtoComSenhaErrada.setIdade(25);
		dtoComSenhaErrada.setFuncao("Atendente");
		dtoComSenhaErrada.setCpf("12345678901");
		dtoComSenhaErrada.setSenha("senha123");
		dtoComSenhaErrada.setConfirmacaoSenha("outrasenha");

		ResponseEntity<?> resposta = controller.updateFuncionario(id, dtoComSenhaErrada);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		assertThat(resposta.getBody()).isInstanceOf(ErroResposta.class);

		ErroResposta erro = (ErroResposta) resposta.getBody();
		assertThat(erro.getCodigo()).isEqualTo("DADOS_INVALIDOS");
		assertThat(erro.getMensagem()).isEqualTo("Senha e confirmação de senha não coincidem");

		// O service não deve ser chamado
		verify(service, never()).updateFuncionario(anyLong(), any(FuncionarioDTO.class));
	}

	// DELETE funcionario

	@Test
	void deleteFuncionario_status200() {
		Long id = 1L;

		// Mocka o existsById para retornar true
		when(service.existsById(id)).thenReturn(true);

		ResponseEntity<?> resposta = controller.deleteFuncionario(id);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resposta.getBody()).isEqualTo("Dados removidos");

		verify(service, times(1)).deleteFuncionario(id);
	}

	@Test
	void deleteFuncionario_status404() {
		Long id = 99L;

		// Mocka o existsById para retornar false
		when(service.existsById(id)).thenReturn(false);

		ResponseEntity<?> resposta = controller.deleteFuncionario(id);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(resposta.getBody()).isInstanceOf(ErroResposta.class);
		ErroResposta erro = (ErroResposta) resposta.getBody();
		assertThat(erro.getCodigo()).isEqualTo("NAO_ENCONTRADO");
		assertThat(erro.getMensagem()).isEqualTo("Funcionário não encontrado");

		// Verifica que delete não foi chamado
		verify(service, never()).deleteFuncionario(id);
	}

}
