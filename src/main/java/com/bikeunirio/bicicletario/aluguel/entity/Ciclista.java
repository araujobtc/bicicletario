package com.bikeunirio.bicicletario.aluguel.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "ciclistas")
public class Ciclista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O status é obrigatório")
    private String status;

    private String nome;
    private LocalDate nascimento;
    private String cpf;
    private String nacionalidade;
    private String email;
    private String urlFotoDocumento;
    private String senha;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passaporte_id")
    private Passaporte passaporte;

    @OneToOne(mappedBy = "ciclista", cascade = CascadeType.ALL)
    private Cartao cartao;

    private LocalDateTime dataConfirmacaoEmail;

    public Long getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getNascimento() {
        return nascimento;
    }

    public void setNascimento(LocalDate nascimento) {
        this.nascimento = nascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlFotoDocumento() {
        return urlFotoDocumento;
    }

    public void setUrlFotoDocumento(String urlFotoDocumento) {
        this.urlFotoDocumento = urlFotoDocumento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Passaporte getPassaporte() {
        return passaporte;
    }

    public void setPassaporte(Passaporte passaporte) {
        this.passaporte = passaporte;
    }

    public MeioDePagamentoDTO getCartao() {
        MeioDePagamentoDTO dto = new MeioDePagamentoDTO();
        dto.setNomeTitular(this.cartao.getNomeTitular());
        dto.setNumero(this.cartao.getNumero());
        dto.setValidade(this.cartao.getValidade());
        dto.setCvv(this.cartao.getCvv());
        return dto;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }

    public LocalDateTime getDataConfirmacaoEmail() {
        return dataConfirmacaoEmail;
    }

    public void setDataConfirmacaoEmail(LocalDateTime dataConfirmacaoEmail) {
        this.dataConfirmacaoEmail = dataConfirmacaoEmail;
    }

}
