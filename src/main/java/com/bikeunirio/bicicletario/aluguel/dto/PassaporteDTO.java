package com.bikeunirio.bicicletario.aluguel.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
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

}