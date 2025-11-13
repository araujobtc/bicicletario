package com.bikeunirio.bicicletario.aluguel.dto;

import java.time.LocalDate;

import com.bikeunirio.bicicletario.aluguel.enums.Nacionalidades;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

public class CiclistaDTO {

	@NotBlank(message = "O nome é obrigatório")
	private String nome;

	@NotNull(message = "A data de nascimento é obrigatória")
	@Past(message = "A data de nascimento deve ser no passado")
	private LocalDate nascimento;

	@Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos")
	private String cpf;

	@NotBlank(message = "A nacionalidade é obrigatória")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
	private Nacionalidades nacionalidade;

	@Email(message = "E-mail inválido")
	@NotBlank(message = "O e-mail é obrigatório")
	private String email;

	@NotBlank(message = "A URL da foto do documento é obrigatória")
	private String urlFotoDocumento;

	@NotBlank(message = "A senha é obrigatória")
	private String senha;

	@Valid
	private PassaporteDTO passaporte;

	@Valid
	private MeioDePagamentoDTO meioDePagamento;

	public String getNome() {
		return nome;
	}

	public LocalDate getNascimento() {
		return nascimento;
	}

	public String getCpf() {
		return cpf;
	}

	public Nacionalidades getNacionalidade() {
		return nacionalidade;
	}

	public String getEmail() {
		return email;
	}

	public String getUrlFotoDocumento() {
		return urlFotoDocumento;
	}

	public String getSenha() {
		return senha;
	}

	public PassaporteDTO getPassaporte() {
		return passaporte;
	}

	public void setPassaporte(PassaporteDTO passaporte) {
		this.passaporte = passaporte;
	}

	public MeioDePagamentoDTO getMeioDePagamento() {
		return meioDePagamento;
	}

	public void setMeioDePagamento(MeioDePagamentoDTO meioDePagamento) {
		this.meioDePagamento = meioDePagamento;
	}
}
