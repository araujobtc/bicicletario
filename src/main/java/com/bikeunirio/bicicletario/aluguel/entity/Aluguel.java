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

@Entity
@Table(name = "alugueis")
public class Aluguel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "ciclista_id")
	// fetch = FetchType.LAZY: o ciclista só é carregado quando getCiclista() é chamado
	private Ciclista ciclista;

	@Column(name = "bicicleta_id", nullable = false)
	private Long bicicletaId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "cartao_id")
	private Cartao cartao;

	@Column(name = "data_inicio", nullable = false)
	private LocalDateTime dataInicio;

	@Column(name = "data_fim")
	private LocalDateTime dataFim;

	@Column(nullable = false)
	private Double valor;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
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
