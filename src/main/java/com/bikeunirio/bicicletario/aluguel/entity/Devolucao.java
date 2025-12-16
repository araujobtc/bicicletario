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
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alugueis")
public class Devolucao {

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
    private LocalDateTime horaInicio;

    @Column(name = "data_fim")
    private LocalDateTime horaFim;

    @Column(name = "cobranca_id")
    private Long cobranca;

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCiclista() {
        return ciclista != null ? ciclista.getId() : null;
    }

    public void setCiclista(Ciclista ciclista) {
        this.ciclista = ciclista;
    }

    public Long getBicicletaId() {
        return bicicletaId;
    }

    public void setBicicletaId(Long bicicletaId) {
        this.bicicletaId = bicicletaId;
    }

    public Long getTrancaInicio() {
        return trancaInicio;
    }

    public void setTrancaInicio(Long trancaInicio) {
        this.trancaInicio = trancaInicio;
    }

    public Long getTrancaFim() {
        return trancaFim;
    }

    public void setTrancaFim(Long trancaFim) {
        this.trancaFim = trancaFim;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalDateTime horaFim) {
        this.horaFim = horaFim;
    }

    public Long getCobranca() {
        return cobranca;
    }

    public void setCobranca(Long cobranca) {
        this.cobranca = cobranca;
    }
}
