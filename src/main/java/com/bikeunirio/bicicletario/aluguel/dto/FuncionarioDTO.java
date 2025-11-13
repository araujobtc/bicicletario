package com.bikeunirio.bicicletario.aluguel.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class FuncionarioDTO {

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotNull(message = "A idade é obrigatória")
	@Min(value = 18, message = "A idade mínima é 18 anos")
	private Integer idade;

	@NotBlank(message = "A função é obrigatória")
	private String funcao;

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

	public String getNome() {
		return nome;
	}

	public Integer getIdade() {
		return idade;
	}

	public String getFuncao() {
		return funcao;
	}

	public String getCpf() {
		return cpf;
	}

	public String getEmail() {
		return email;
	}

	public String getSenha() {
		return senha;
	}

	public String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}
}
