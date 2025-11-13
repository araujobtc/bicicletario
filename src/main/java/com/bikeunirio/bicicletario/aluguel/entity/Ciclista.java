package com.bikeunirio.bicicletario.aluguel.entity;

import java.time.LocalDate;

import com.bikeunirio.bicicletario.aluguel.enums.Nacionalidades;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ciclistas")
public class Ciclista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate nascimento;
    private String cpf;
    
    @Enumerated(EnumType.STRING)
	private Nacionalidades nacionalidade;
    
    private String email;
    private String urlFotoDocumento;
    private String senha;

    @OneToOne(cascade = CascadeType.ALL) 
    @JoinColumn(name = "passaporte_id") 
    private Passaporte passaporte;

    @OneToOne(mappedBy = "ciclista", cascade = CascadeType.ALL)
    private Cartao cartao;

    public Long getId() {
        return id;
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

    public Nacionalidades getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(Nacionalidades nacionalidade) {
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

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
}
