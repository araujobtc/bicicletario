package com.bikeunirio.bicicletario.aluguel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;

@RestController
@RequestMapping("/ciclista")
public class CiclistaController {

    @Autowired
    private CiclistaService ciclistaService;
    
    @PostMapping
    public void createCiclista() {
    	
    }
    
    @GetMapping("/{idCiclista}")
    public void readCiclista() {
    	
    }
    
    @PutMapping("/{idCiclista}")
    public void updateCiclista() {
    	
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
