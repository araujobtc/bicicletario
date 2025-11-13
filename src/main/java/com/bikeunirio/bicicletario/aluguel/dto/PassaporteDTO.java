package com.bikeunirio.bicicletario.aluguel.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PassaporteDTO {

	@NotBlank(message = "O número do passaporte é obrigatório")
	private String numero;

	@NotNull(message = "A validade do passaporte é obrigatória")
	private LocalDate validade;

	@NotBlank(message = "O país é obrigatório")
	private String pais;

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public LocalDate getValidade() {
		return validade;
	}

	public void setValidade(LocalDate validade) {
		this.validade = validade;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}
}