package com.bikeunirio.bicicletario.aluguel.entity;

import java.time.LocalDateTime;

import com.bikeunirio.bicicletario.aluguel.enums.StatusAluguel;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alugueis")
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O ciclista é obrigatório")
    @Valid
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
	// fetch = FetchType.LAZY: o ciclista só é carregado quando getCiclista() é chamado
    @JoinColumn(name = "ciclista_id")
    private Ciclista ciclista;

    @NotNull(message = "O ID da bicicleta é obrigatório")
    @Column(name = "bicicleta_id", nullable = false)
    private Long bicicletaId;

    @Valid
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;

    @Column(name = "data_fim")
    private LocalDateTime dataFim;

    @NotNull(message = "O valor é obrigatório")
    @Min(value = 0, message = "O valor deve ser positivo")
    @Column(nullable = false)
    private Double valor;

    @NotNull(message = "O status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAluguel status;

    public Long getId() {
        return id;
    }

    public Ciclista getCiclista() {
        return ciclista;
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

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public StatusAluguel getStatus() {
        return status;
    }

    public void setStatus(StatusAluguel status) {
        this.status = status;
    }
}
