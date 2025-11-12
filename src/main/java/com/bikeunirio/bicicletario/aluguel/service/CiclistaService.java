package com.bikeunirio.bicicletario.aluguel.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.dto.PassaporteDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Passaporte;
import com.bikeunirio.bicicletario.aluguel.repository.CiclistaRepository;

@Service
public class CiclistaService {
	
	CiclistaRepository ciclistaRepository;

    public Ciclista createCiclista(CiclistaDTO ciclistaDTO) {
        Ciclista ciclista = new Ciclista();

        BeanUtils.copyProperties(ciclistaDTO, ciclista, "id", "passaporte", "meioDePagamento", "cartao");

        PassaporteDTO passaporteDTO = ciclistaDTO.getPassaporte();
        if (passaporteDTO != null) {
            Passaporte passaporte = new Passaporte();
            BeanUtils.copyProperties(passaporteDTO, passaporte, "id");
            ciclista.setPassaporte(passaporte);
        }

        MeioDePagamentoDTO meioDePagamentoDTO = ciclistaDTO.getMeioDePagamento();
        if (meioDePagamentoDTO != null) {
            Cartao cartao = new Cartao();
            BeanUtils.copyProperties(meioDePagamentoDTO, cartao, "id");
            // Isso garante que a FK 'ciclista_id' no Cartao seja preenchida.
            cartao.setCiclista(ciclista);
            ciclista.setCartao(cartao);
        }

        return ciclistaRepository.save(ciclista);
    }

	public Optional<Ciclista> readCiclista(long id) {
		return ciclistaRepository.findById(id);
	}
	
}
