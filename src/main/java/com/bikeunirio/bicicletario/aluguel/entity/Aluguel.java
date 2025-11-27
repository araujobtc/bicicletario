package com.bikeunirio.bicicletario.aluguel.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "alugueis")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O ciclista é obrigatório")
    @Valid
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ciclista_id")
    private Ciclista ciclista;

    @NotNull(message = "O ID da bicicleta é obrigatório")
    @Column(name = "bicicleta_id", nullable = false)
    private Long bicicletaId;

    @NotNull(message = "A tranca de início é obrigatória")
    @Column(name = "tranca_inicio", nullable = false)
    private Long trancaInicio;

    @Column(name = "tranca_fim")
    private Long trancaFim;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @NotNull(message = "O valor é obrigatório")
    @Min(value = 0, message = "O valor deve ser positivo")
    @Column(nullable = false)
    private Double cobranca;

}
