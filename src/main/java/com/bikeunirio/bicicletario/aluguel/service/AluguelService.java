package com.bikeunirio.bicicletario.aluguel.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
	
	private CiclistaService ciclistaService;

	EquipamentosService equipamentosService;
	
	ExternoService externoService;

	public AluguelService(AluguelRepository aluguelRepository, CiclistaService ciclistaService, ExternoService externoService, EquipamentosService equipamentosService) {
		this.aluguelRepository = aluguelRepository;
		this.ciclistaService = ciclistaService;
		this.equipamentosService = equipamentosService;
		this.externoService = externoService;
	}

	// GET bicicleta alugada

	public Optional<BicicletaDTO> getBicicletaPorIdCiclista(Long idCiclista) {
		Optional<Aluguel> responseAluguel = aluguelRepository.findByCiclistaIdAndHoraFimIsNull(idCiclista);

		if (responseAluguel.isEmpty()) {
			return Optional.empty();
		}
		Aluguel aluguel = responseAluguel.get();

		return equipamentosService.getBicicletaPorId(aluguel.getBicicletaId());
	}

	public boolean isCiclistaComAluguelAtivo(Long idCiclista) {
		Optional<Aluguel> responseAluguel = aluguelRepository.findByCiclistaIdAndHoraFimIsNull(idCiclista);
		if (responseAluguel.isEmpty()) {
			return false;
		}

		return true;
	}

	// alugar
	public ResponseEntity<Object> alugarBicicleta(Long trancaInicio, Ciclista ciclista) {
		Optional<Long> responseBicicleta = equipamentosService.getBicicletaPorIdTranca(trancaInicio);
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
		aluguel.setHoraInicio(LocalDateTime.now());
		aluguel.getCobrancas().add(idCobranca);

		externoService.enviarEmail(ciclista.getEmail(), "ALUGOU eeeeeeh!!!");
		
		return ResponseEntity.ok(aluguelRepository.save(aluguel));

	}
	
	// devolver

	public ResponseEntity<Object> devolverBicicleta(Long idBicicleta, Long idTranca) {
	    Optional<Aluguel> responseAluguel = aluguelRepository.findByBicicletaIdAndHoraFimIsNull(idBicicleta);
	    if (responseAluguel.isEmpty()) {
	        return GlobalExceptionHandler.unprocessableEntity("Esta bicicleta não tem aluguel ativo");
	    }

	    Aluguel aluguel = responseAluguel.get();

	    LocalDateTime agora = LocalDateTime.now();
	    aluguel.setHoraFim(agora);
	    aluguel.setTrancaFim(idTranca);

	    long minutosUso = java.time.Duration.between(aluguel.getHoraInicio(), agora).toMinutes();
	    double valorExtra = 0.0;
	    if (minutosUso > 120) { // mais de 2 horas
	        long meiaHorasExtras = (minutosUso - 120) / 30;
	        valorExtra = meiaHorasExtras * 5.0; // R$ 5,00 por cada meia hora extra
	        Long cobrancaId = externoService.cobrar(aluguel.getCiclista(), valorExtra);
	        aluguel.getCobrancas().add(cobrancaId);
	    }

	    Aluguel aluguelAtualizado = aluguelRepository.save(aluguel);

	    equipamentosService.atualizarStatusBicicleta(idBicicleta, "DISPONIVEL");
	    equipamentosService.atualizarStatusTranca(idTranca);
	    
	    Optional<Ciclista> responseCiclista = ciclistaService.readCiclista(aluguel.getCiclista());
	    
	    if(responseCiclista.isEmpty()) {
			HttpHeaders headers = new HttpHeaders();
			headers.add("X-Success-Message", "Dados do ciclista nao foram encontrados");

			return new ResponseEntity<>(headers, HttpStatus.OK);
	    }

	    externoService.enviarEmail(responseCiclista.get().getEmail(), "bicicleta devolvida!");

	    return ResponseEntity.ok(aluguelAtualizado);
	}


}
