package com.bikeunirio.bicicletario.aluguel.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AluguelRequestDTO {
    @NotNull(message = "O ID do ciclista é obrigatório")
    private Long ciclista;

    @NotNull(message = "O ID da tranca de início é obrigatório")
    private Long trancaInicio;
}
