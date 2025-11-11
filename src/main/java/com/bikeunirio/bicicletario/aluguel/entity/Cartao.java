package com.bikeunirio.bicicletario.aluguel.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "cartoes")
public class Cartao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do titular é obrigatório")
    private String nomeTitular;

    @NotBlank(message = "O número do cartão é obrigatório")
    @Pattern(regexp = "\\d{16}", message = "O número do cartão deve conter 16 dígitos")
    private String numero;

    @NotNull(message = "A data de validade é obrigatória")
    @Future(message = "A data de validade deve ser posterior a data atual")
    private LocalDate validade;

    @NotBlank(message = "O código de segurança (CVV) é obrigatório")
    @Pattern(regexp = "\\d{3,4}", message = "O CVV deve conter 3 ou 4 dígitos")
    private String cvv;

    @OneToOne
    @JoinColumn(name = "ciclista_id", nullable = false)
    @NotNull(message = "O ciclista associado é obrigatório")
    private Ciclista ciclista;

    public Long getId() {
        return id;
    }

    public String getNomeTitular() {
        return nomeTitular;
    }

    public void setNomeTitular(String nomeTitular) {
        this.nomeTitular = nomeTitular;
    }

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

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public Ciclista getCiclista() {
        return ciclista;
    }

    public void setCiclista(Ciclista ciclista) {
        this.ciclista = ciclista;
    }
}
