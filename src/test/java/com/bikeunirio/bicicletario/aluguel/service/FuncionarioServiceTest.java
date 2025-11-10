package com.bikeunirio.bicicletario.aluguel.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.repository.FuncionarioRepository;

@ExtendWith(MockitoExtension.class)
public class FuncionarioServiceTest {
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
    private FuncionarioService funcionarioService;
    
    @Mock
    private FuncionarioRepository funcionarioRepository; // Mocka o Repository

    // GET funcionarios
    @Test
    void deveRetornarTodosOsFuncionarios() {
        when(funcionarioRepository.findAll()).thenReturn(Arrays.asList(FUNCIONARIO_VALIDO));

        List<Funcionario> resultado = funcionarioService.getAllFuncionarios();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Isabelle");
        
        verify(funcionarioRepository, times(1)).findAll();
    }

    // POST funcionario
    @Test
    void deveCriarFuncionarioComSucesso() {
        when(funcionarioRepository.save(FUNCIONARIO_VALIDO)).thenReturn(FUNCIONARIO_VALIDO);

        Funcionario resultado = funcionarioService.createFuncionario(FUNCIONARIO_VALIDO);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("Isabelle");
        verify(funcionarioRepository, times(1)).save(FUNCIONARIO_VALIDO);
    }
}