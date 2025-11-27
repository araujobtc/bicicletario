package com.bikeunirio.bicicletario.aluguel.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DevolucaoRequestDTO {
    @NotNull(message = "O ID da tranca é obrigatório")
    private Long idTranca;

    @NotNull(message = "O ID da bicicleta é obrigatório")
    private Long idBicicleta;
}
