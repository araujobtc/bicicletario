package com.bikeunirio.bicicletario.aluguel.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.repository.CartaoRepository;

@Service
public class CartaoService {

	private CartaoRepository repository;
	
	public CartaoService(CartaoRepository repository) {
		this.repository = repository;
	}

	public Optional<Cartao> updateCartao(long idCiclista, MeioDePagamentoDTO meioDePagamentoDTO) {
		return repository.findByCiclistaId(idCiclista).map(cartao -> {
			BeanUtils.copyProperties(meioDePagamentoDTO, cartao, "id");
			return repository.save(cartao);
		});
	}
	
	public Optional<MeioDePagamentoDTO> getCartao(long idCiclista){
		return repository.findByCiclistaId(idCiclista).map(cartao -> {
			MeioDePagamentoDTO dto = new MeioDePagamentoDTO();
			BeanUtils.copyProperties(cartao, dto);
			return dto;
		});
	}
	
}
