package com.bikeunirio.bicicletario.aluguel.enums;

import org.springframework.beans.BeanUtils;

import com.bikeunirio.bicicletario.aluguel.dto.FuncionarioDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;

public class FuncionarioExemplos {

    public static final Funcionario FUNCIONARIO;
    public static final FuncionarioDTO FUNCIONARIO_DTO;
    
    static {
        FUNCIONARIO = new Funcionario();
        FUNCIONARIO.setNome("Isabelle");
        FUNCIONARIO.setEmail("isa@exemplo.com");
        FUNCIONARIO.setIdade(25);
        FUNCIONARIO.setFuncao("Atendente");
        FUNCIONARIO.setCpf("12345678901");
        FUNCIONARIO.setSenha("senha123");
        
    	FUNCIONARIO_DTO = new FuncionarioDTO();

		BeanUtils.copyProperties(FUNCIONARIO, FUNCIONARIO_DTO, "id");
    }

}
