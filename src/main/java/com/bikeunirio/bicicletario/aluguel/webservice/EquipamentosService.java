package com.bikeunirio.bicicletario.aluguel.webservice;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;

@Service
public class EquipamentosService {

	public Optional<BicicletaDTO> getBicicletaPorId(Long idBicicleta){
		if(idBicicleta != 1L) {
			return Optional.empty();
		}
		
		BicicletaDTO bicicleta = new BicicletaDTO();
		
		bicicleta.setId(1L);
		bicicleta.setMarca("Caloi");
		bicicleta.setModelo("Explorer");
		bicicleta.setAno("2023");
		bicicleta.setNumero(42);
		bicicleta.setStatus("DISPONIVEL");
		
		return Optional.of(bicicleta);
	}

	public Optional<Long> getBicicletaPorIdTranca(Long idTranca) {
		return Optional.of(1L);
	}
	
	public boolean isTrancaDisponivel(Long idTranca) {
		return true;
	}

	public boolean atualizarStatusBicicleta(Long idBicicleta, String string) {
		return true;
	}

	public boolean atualizarStatusTranca(Long idTranca) {
		return true;
	}
}
