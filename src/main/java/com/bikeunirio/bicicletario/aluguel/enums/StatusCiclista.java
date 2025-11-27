package com.bikeunirio.bicicletario.aluguel.enums;

public enum StatusCiclista {

	ATIVO("Ativo"), CADASTRADO("Cadastrado");

	private final String descricao;

	private StatusCiclista(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}