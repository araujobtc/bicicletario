package com.bikeunirio.bicicletario.aluguel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.service.AluguelService;

@RestController
@RequestMapping("/")
public class AluguelController {
	
    @Autowired
    private AluguelService aluguelService ;

	@PostMapping("/aluguel")
	public void alugarBicicleta() {
		
	}

	@PostMapping("/devolucao")
	public void devolverBicicleta() {
		
	}
	// mater apenas se apagar dados somente relacionado a aluguel
	@PostMapping("/restaurarBanco")
	public void restaurarBD() {
		
	}
}
