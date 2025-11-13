package com.bikeunirio.bicicletario.aluguel.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class MeioDePagamentoDTO {

	@NotBlank(message = "O nome do titular é obrigatório")
	private String nomeTitular;

	@NotBlank(message = "O número do cartão é obrigatório")
	@Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter 16 dígitos")
	private String numero;

	@NotNull(message = "A validade do cartão é obrigatória")
	@Future(message = "A data de validade deve ser posterior a data atual")
	private LocalDate validade;

	@NotBlank(message = "O CVV é obrigatório")
	@Pattern(regexp = "\\d{3,4}", message = "O CVV deve conter 3 ou 4 dígitos")
	private String cvv;

	public String getNomeTitular() {
		return nomeTitular;
	}

	public String getNumero() {
		return numero;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public String getCvv() {
		return cvv;
	}
}
