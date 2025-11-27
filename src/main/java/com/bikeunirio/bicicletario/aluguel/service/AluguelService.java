package com.bikeunirio.bicicletario.aluguel.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@Service
public class AluguelService {

	AluguelRepository aluguelRepository;

	EquipamentosService equipamentosService;
	
	ExternoService externoService;

	public AluguelService(AluguelRepository aluguelRepository, ExternoService externoService, EquipamentosService equipamentosService) {
		this.aluguelRepository = aluguelRepository;
		this.equipamentosService = equipamentosService;
		this.externoService = externoService;
	}

	// GET bicicleta alugada

	public Optional<BicicletaDTO> getBicicletaPorIdCiclista(Long idCiclista) {
		Optional<Aluguel> responseAluguel = aluguelRepository.findByCiclistaIdAndDataFimIsNull(idCiclista);

		if (responseAluguel.isEmpty()) {
			return Optional.empty();
		}
		Aluguel aluguel = responseAluguel.get();

		return equipamentosService.getBicicletaPorId(aluguel.getBicicletaId());
	}

	public boolean isCiclistaComAluguelAtivo(Long idCiclista) {
		Optional<Aluguel> responseAluguel = aluguelRepository.findByCiclistaIdAndDataFimIsNull(idCiclista);
		if (responseAluguel.isEmpty()) {
			return false;
		}

		return true;
	}

	public ResponseEntity<Object> alugarBicicleta(Long trancaInicio, Ciclista ciclista) {
		Optional<Long> responseBicicleta = equipamentosService.getBicicleta(trancaInicio);
		if (responseBicicleta.isEmpty()) {
			return GlobalExceptionHandler.unprocessableEntity("Não existe bicicleta disponível na tranca");
		}
		Long bicicletaId = responseBicicleta.get();

		// COMENT: editar na prox
		Long idCobranca = externoService.realizarCobranca(ciclista.getId(), 10.0);
		if (idCobranca == null) {
			return GlobalExceptionHandler.unprocessableEntity("Falha na cobrança");
		}

		Aluguel aluguel = new Aluguel();
		aluguel.setCiclista(ciclista);
		aluguel.setBicicletaId(bicicletaId);
		aluguel.setTrancaInicio(trancaInicio);
		aluguel.setDataInicio(LocalDateTime.now());
		aluguel.getCobrancas().add(idCobranca);

		externoService.enviarEmail(ciclista.getEmail(), "ALUGOU eeeeeeh!!!");
		
		return ResponseEntity.ok(aluguel);

	}

}
