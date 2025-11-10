package com.bikeunirio.bicicletario.aluguel.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

@ExtendWith(MockitoExtension.class)
public class FuncionarioControllerTest {
	public static final Funcionario FUNCIONARIO_VALIDO;

	static {
		FUNCIONARIO_VALIDO = new Funcionario();
		FUNCIONARIO_VALIDO.setNome("Isabelle");
		FUNCIONARIO_VALIDO.setEmail("isa@exemplo.com");
		FUNCIONARIO_VALIDO.setIdade(25);
		FUNCIONARIO_VALIDO.setFuncao("Atendente");
		FUNCIONARIO_VALIDO.setCpf("12345678901");
		FUNCIONARIO_VALIDO.setSenha("senha123");
	}

	@InjectMocks
	private FuncionarioController controller;

	@Mock
	private FuncionarioService service; // Mocka o Service

	// GET funcionarios
	@Test
	void Status200RetornarListaDeFuncionarios() {
		List<Funcionario> funcionarios = Arrays.asList(FUNCIONARIO_VALIDO);

		when(service.getAllFuncionarios()).thenReturn(funcionarios);

		ResponseEntity<List<Funcionario>> resposta = controller.getAllFuncionarios();

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resposta.getBody()).hasSize(1);
		assertThat(resposta.getBody().get(0).getNome()).isEqualTo("Isabelle");
	}

	// POST funcionario
	@Test
	void deveCriarFuncionarioComSucesso() {
		when(service.createFuncionario(FUNCIONARIO_VALIDO)).thenReturn(FUNCIONARIO_VALIDO);

		ResponseEntity<Funcionario> resposta = controller.createFuncionario(FUNCIONARIO_VALIDO);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(resposta.getBody().getNome()).isEqualTo("Isabelle");
	}

	@Test
	void deveRetornar422QuandoDadosInvalidos() {
		// Arrange: cria um funcionário inválido com todos os campos obrigatórios ausentes
		Funcionario funcionarioInvalido = new Funcionario();

		// Cria o BeanPropertyBindingResult para armazenar os erros de validação
		BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(funcionarioInvalido, "funcionario");

		// Adiciona erros de validação manualmente (simulando erros do @Valid)
		bindingResult.addError(new FieldError("funcionario", "nome", "O nome é obrigatório"));
		bindingResult.addError(new FieldError("funcionario", "email", "O e-mail é obrigatório"));

		// Mocka a exceção que normalmente seria lançada pelo Spring ao validar o @RequestBody
		MethodArgumentNotValidException ex = org.mockito.Mockito.mock(MethodArgumentNotValidException.class);
		when(ex.getBindingResult()).thenReturn(bindingResult);

		// chama o handler que converte os erros em ResponseEntity
		ResponseEntity<List<ErroResposta>> resposta = controller.handleValidationExceptions(ex);

		assertThat(resposta.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		assertThat(resposta.getBody()).isNotNull();
		assertThat(resposta.getBody()).hasSize(2);
		assertThat(resposta.getBody().get(0).getMensagem()).isEqualTo("O nome é obrigatório");
		assertThat(resposta.getBody().get(1).getMensagem()).isEqualTo("O e-mail é obrigatório");
	}

}
