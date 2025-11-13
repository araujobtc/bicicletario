package com.bikeunirio.bicicletario.aluguel.enums;

public enum FuncoesFuncionario {

	ADMINISTRATIVO("administrativo"), REPARADOR("reparador");

	private final String valor;

	private FuncoesFuncionario(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}
}