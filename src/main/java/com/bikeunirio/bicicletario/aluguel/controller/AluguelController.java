package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.AluguelRequestDTO;
import com.bikeunirio.bicicletario.aluguel.dto.DevolucaoRequestDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.AluguelService;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class AluguelController {

	private AluguelService aluguelService;
	private CiclistaService ciclistaService;
	private EquipamentosService equipamentosService;

	public AluguelController(AluguelService aluguelService, CiclistaService ciclistaService,
			EquipamentosService equipamentosService, ExternoService externoService) {
		this.aluguelService = aluguelService;
		this.ciclistaService = ciclistaService;
		this.equipamentosService = equipamentosService;
	}

	// UC16

	// Caso de uso 03
	@PostMapping("/aluguel")
	public ResponseEntity<Object> alugarBicicleta(@RequestBody @Valid AluguelRequestDTO aluguelRequestDTO) {
		Long idCiclista = aluguelRequestDTO.getCiclista();
		Long trancaInicio = aluguelRequestDTO.getTrancaInicio();

		Optional<Ciclista> responseCiclista = ciclistaService.readCiclista(idCiclista);
		if (responseCiclista.isEmpty()) {
			return GlobalExceptionHandler.notFound("Ciclista não encontrado");
		}
		Ciclista ciclista = responseCiclista.get();

		if (!StatusCiclista.ATIVO.getDescricao().equals(ciclista.getStatus())) {
			return GlobalExceptionHandler.unprocessableEntity("Ciclista não ativo para alugar bicicleta");
		}

		if (aluguelService.isCiclistaComAluguelAtivo(idCiclista)) {
			return GlobalExceptionHandler.unprocessableEntity("Ciclista já possui um aluguel ativo");
		}

		return aluguelService.alugarBicicleta(trancaInicio, ciclista);
	}

	// Casos de uso UC04 e UC16
	@PostMapping("/devolucao")
	public ResponseEntity<Object> devolverBicicleta(@RequestBody @Valid DevolucaoRequestDTO devolucaoRequestDTO) {
		Long idBicicleta = devolucaoRequestDTO.getIdBicicleta();
		Long idTranca = devolucaoRequestDTO.getIdTranca();

		if (!equipamentosService.isTrancaDisponivel(idTranca)) {
			return GlobalExceptionHandler.unprocessableEntity("Esta tranca não esta disponivel");
		}

		return aluguelService.devolverBicicleta(idBicicleta, idTranca);
	}

	@PostMapping("/restaurarBanco")
	public void restaurarBD() {
	}
}
