package com.bikeunirio.bicicletario.aluguel.webservice;

import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;

@Service
public class ExternoService {

	public boolean enviarEmail(String emailDestinatario, String Conteudo) {
		return true;
	}
	
	public boolean isCartaoInvalido(MeioDePagamentoDTO cartao) {
		return true;
	}
}
