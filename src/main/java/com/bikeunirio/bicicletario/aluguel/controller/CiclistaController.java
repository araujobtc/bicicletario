package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.exception.GlobalExceptionHandler;
import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/ciclista")
public class CiclistaController {

    @Autowired
    private CiclistaService ciclistaService;
    
    @PostMapping
    public ResponseEntity<Ciclista> createCiclista(@Valid @RequestBody CiclistaDTO ciclistaDTO) {
        Ciclista ciclista = ciclistaService.createCiclista(ciclistaDTO);
        
        return new ResponseEntity<>(ciclista, HttpStatus.CREATED);
    }
    
    @GetMapping("/{idCiclista}")
    public ResponseEntity<?> readCiclista(@PathVariable Long idCiclista) {
		Optional<Ciclista> ciclista = ciclistaService.readCiclista(idCiclista);

		if (ciclista.isPresent()) {
			return ResponseEntity.ok(ciclista.get()); // 200
		} else {
			return GlobalExceptionHandler.notFound("Ciclista não encontrado");
		}
    	
    }
    
    @PutMapping("/{idCiclista}")
    public ResponseEntity<?> updateCiclista(@PathVariable Long idCiclista, @RequestBody @Valid CiclistaDTO ciclistaDTO) {
		Optional<Ciclista> atualizado = ciclistaService.updateCiclista(idCiclista, ciclistaDTO);

		if (atualizado.isPresent()) {
			return ResponseEntity.ok(atualizado.get()); // 200
		} else {
			return GlobalExceptionHandler.notFound("Ciclista não encontrado");
		}
	}
    
    @PostMapping("/{idCiclista}/ativar")
    public void ativar() {
    	
    }
    
    @GetMapping("/{idCiclista}/permiteAluguel}")
    public void ciclistaTemPermissao() {
    	
    }
    
    @GetMapping("/{idCiclista}")
    public void getBicicletaAlugada() {
    	
    }
    
    @GetMapping("/existeEmail/{email}")
    public void isEmailCadastrado() {
    	
    }
}
