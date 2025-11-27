package com.bikeunirio.bicicletario.aluguel.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.dto.PassaporteDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Passaporte;
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.repository.CiclistaRepository;

@Service
public class CiclistaService {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(CiclistaService.class);

    private CiclistaRepository ciclistaRepository;

	public CiclistaService(CiclistaRepository ciclistaRepository) {
		this.ciclistaRepository = ciclistaRepository;
	}

	public boolean existsByEmail(String email) {
		return ciclistaRepository.existsByEmail(email);
	}

	public boolean existsById(Long id) {
		return ciclistaRepository.existsById(id);
	}

	/**/

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

		ciclista.setStatus(StatusCiclista.CADASTRADO.getDescricao());

		return ciclistaRepository.save(ciclista);
	}

	public Optional<Ciclista> readCiclista(Long id) {
		return ciclistaRepository.findById(id);
	}

	public Optional<Ciclista> updateCiclista(Long id, CiclistaDTO ciclistaDTO) {
		return ciclistaRepository.findById(id).map(ciclista -> {
			BeanUtils.copyProperties(ciclistaDTO, ciclista, "id");
			return ciclistaRepository.save(ciclista);
		});
	}

	public Optional<Ciclista> ativarCadastroCiclista(Long idCiclista) {
		return ciclistaRepository.findById(idCiclista).map(ciclista -> {
			ciclista.setStatus(StatusCiclista.ATIVO.getDescricao());
			ciclista.setDataConfirmacaoEmail(LocalDateTime.now());
			return ciclistaRepository.save(ciclista);
		});
	}

	// cod

	public boolean isCodigoValido(Long codeRequest, Ciclista ciclista) {
		Long code = Long.parseLong(gerarCodigo(ciclista).toString());

		if (code.equals(codeRequest)) {
			return true;
		}
		LOGGER.info("Errou, na verdade é {}", code);

		return false;
	}

	public Integer gerarCodigo(Ciclista ciclista) {
		String base = (ciclista.getId() != null ? ciclista.getId().toString() : "")
				+ (ciclista.getCpf() != null ? ciclista.getCpf() : "")
				+ (ciclista.getNascimento() != null ? ciclista.getNascimento().toString() : "");

		// Calcula hashCode do String e converte para Integer positivo
		int hash = base.hashCode();
		return Math.abs(hash);
	}
}
