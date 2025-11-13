package com.bikeunirio.bicicletario.aluguel.enums;

public enum Nacionalidades {

	BRASILEIRO("Brasileiro"), ESTRANGEIRO("Estrangeiro");

	private final String valor;

	private Nacionalidades(String valor) {
		this.valor = valor;
	}

	public String getValor() {
		return valor;
	}
}