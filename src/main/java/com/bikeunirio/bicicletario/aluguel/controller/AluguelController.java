package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.AluguelRequestDTO;
import com.bikeunirio.bicicletario.aluguel.dto.DevolucaoRequestDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Devolucao;
import com.bikeunirio.bicicletario.aluguel.enums.StatusCiclista;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.AluguelService;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/")
public class AluguelController {

    private AluguelService aluguelService;
    private CiclistaService ciclistaService;
    private EquipamentosService equipamentosService;

    public AluguelController(AluguelService aluguelService, CiclistaService ciclistaService,
            EquipamentosService equipamentosService) {
        this.aluguelService = aluguelService;
        this.ciclistaService = ciclistaService;
        this.equipamentosService = equipamentosService;
    }

    // UC16

    // Caso de uso 03
    @PostMapping("/aluguel")
    public ResponseEntity<Object> alugarBicicleta(
            @RequestBody @Valid AluguelRequestDTO dto) {

        Optional<Ciclista> ciclistaOpt = ciclistaService.readCiclista(dto.getCiclista());

        if (ciclistaOpt.isEmpty()) {
            return GlobalExceptionHandler.notFound("Ciclista não encontrado");
        }

        Ciclista ciclista = ciclistaOpt.get();

        if (!StatusCiclista.ATIVO.getDescricao().equals(ciclista.getStatus())) {
            return GlobalExceptionHandler.unprocessableEntity(
                    "Ciclista não está ativo");
        }

        if (aluguelService.isCiclistaComAluguelAtivo(ciclista.getId())) {
            return GlobalExceptionHandler.unprocessableEntity(
                    "Ciclista já possui aluguel ativo");
        }

        Optional<Aluguel> aluguelOpt = aluguelService.alugar(dto.getTrancaInicio(), ciclista);

        if (aluguelOpt.isEmpty()) {
            return GlobalExceptionHandler.unprocessableEntity(
                    "Falha ao realizar aluguel");
        }

        return ResponseEntity.ok(aluguelOpt.get());
    }


    // Casos de uso UC04 e UC16
    @PostMapping("/devolucao")
    public ResponseEntity<Object> devolverBicicleta(
            @RequestBody @Valid DevolucaoRequestDTO dto) {

        if (!equipamentosService.isTrancaDisponivel(dto.getIdTranca())) {
            return GlobalExceptionHandler.unprocessableEntity(
                    "Tranca indisponível");
        }

        Optional<Devolucao> devolucaoOpt = aluguelService.devolver(dto.getIdBicicleta(), dto.getIdTranca());

        if (devolucaoOpt.isEmpty()) {
            return GlobalExceptionHandler.unprocessableEntity(
                    "Bicicleta não possui aluguel ativo");
        }

        return ResponseEntity.ok(devolucaoOpt.get());
    }


    // restaurar banco nao foi desenvolvido
}
