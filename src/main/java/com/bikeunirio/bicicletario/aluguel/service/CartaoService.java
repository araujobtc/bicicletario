package com.bikeunirio.bicicletario.aluguel.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.repository.CartaoRepository;

@Service
public class CartaoService {

	CartaoRepository repository;

	public Optional<Cartao> updateCartao(long idCiclista, MeioDePagamentoDTO meioDePagamentoDTO) {
		return repository.findByIdCiclista(idCiclista).map(cartao -> {
			BeanUtils.copyProperties(meioDePagamentoDTO, cartao, "id");
			return repository.save(cartao);
		});
	}
}
