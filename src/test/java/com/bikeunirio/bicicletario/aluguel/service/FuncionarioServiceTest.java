package com.bikeunirio.bicicletario.aluguel.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;

public class FuncionarioServiceTest {

    private FuncionarioService service;

    @BeforeEach
    void setUp() {
        this.service = new FuncionarioService();
    }

    @Test
    void deveRetornarNullQuandoIdDiferenteDeUm() {
        Long idInvalido = 99L;

        Funcionario resultado = service.buscarFuncionarioPorId(idInvalido);

        assertNull(resultado, "O funcionário deve ser nulo para um ID diferente de 1L.");
    }

    @Test
    void deveRetornarNullQuandoIdForNulo() {
        Funcionario resultado = service.buscarFuncionarioPorId(null);

        assertNull(resultado, "O funcionário deve ser nulo quando o ID for nulo.");
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setEmail("valido@teste.com");
        novoFuncionario.setCpf("11122233344");

        assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(novoFuncionario);
        }, "Deve lançar exceção quando o Nome for nulo.");
    }

    @Test
    void deveLancarExcecaoQuandoEmailForVazio() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome("Nome Valido");
        novoFuncionario.setEmail(" "); // E-mail vazio (trim().isEmpty() deve capturar)
        novoFuncionario.setCpf("11122233344");

        IllegalArgumentException excecao = assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(novoFuncionario);
        }, "Deve lançar exceção quando o Email for vazio.");

        assertEquals("O e-mail do funcionário é obrigatório para o cadastro.", excecao.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoCpfForNulo() {
        Funcionario novoFuncionario = new Funcionario();
        novoFuncionario.setNome("Nome Valido");
        novoFuncionario.setEmail("valido@teste.com");

        assertThrows(IllegalArgumentException.class, () -> {
            service.cadastrarFuncionario(novoFuncionario);
        }, "Deve lançar exceção quando o CPF for nulo.");
    }
}