package com.bikeunirio.bicicletario.aluguel.dto;

import lombok.Data;

@Data
public class BicicletaDTO {

    private Long id;
    private String marca;
    private String modelo;
    private String ano;
    private Integer numero;
    private String status;
}
