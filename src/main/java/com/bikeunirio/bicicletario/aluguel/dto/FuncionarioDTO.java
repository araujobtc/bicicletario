package com.bikeunirio.bicicletario.aluguel.dto;

import com.bikeunirio.bicicletario.aluguel.enums.FuncoesFuncionario;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class FuncionarioDTO {

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotNull(message = "A idade é obrigatória")
	@Min(value = 18, message = "A idade mínima é 18 anos")
	private Integer idade;

	@NotBlank(message = "A função é obrigatória")
	@Enumerated(EnumType.STRING)
	private FuncoesFuncionario funcao;

	@NotBlank(message = "O CPF é obrigatório")
	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos")
	private String cpf;

	@NotBlank(message = "O e-mail é obrigatório")
	@Email(message = "E-mail inválido")
	private String email;

	@NotBlank(message = "A senha é obrigatória")
	private String senha;

	@NotBlank(message = "A confirmação de senha é obrigatória")
	private String confirmacaoSenha;

}