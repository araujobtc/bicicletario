package com.bikeunirio.bicicletario.aluguel.enums;

public enum StatusAluguel {

	ATIVO("ativo"), FINALIZADO("finalizado"), CANCELADO("cancelado");

	private final String descricao;

	private StatusAluguel(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}