package com.bikeunirio.bicicletario.aluguel.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;

@Service
public class AluguelService {

	AluguelRepository aluguelRepository;
	
	EquipamentosService equipamentosService;
	
	public AluguelService(AluguelRepository aluguelRepository, EquipamentosService equipamentosService) {
		this.aluguelRepository = aluguelRepository;
		this.equipamentosService = equipamentosService;
	}
	
	// GET bicicleta alugada
	
	public Optional<BicicletaDTO> getBicicletaPorIdCiclista(Long idCiclista) {
		Optional<Aluguel> responseAluguel = aluguelRepository.findByCiclistaIdAndDataFimIsNull(idCiclista);
		
		if(responseAluguel.isEmpty()) {
			return Optional.empty();
		}
		Aluguel aluguel = responseAluguel.get();
		
		return equipamentosService.getBicicletaPorId(aluguel.getBicicletaId());
	}
	
	
	public boolean isCiclistaComAluguelAtivo(Long idCiclista) {
		Optional<Aluguel> responseAluguel = aluguelRepository.findByCiclistaIdAndDataFimIsNull(idCiclista);
		if(responseAluguel.isEmpty()) {
			return false;
		}
		
		return true;
	}

}
