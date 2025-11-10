package com.bikeunirio.bicicletario.aluguel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bikeunirio.bicicletario.aluguel.dto.ErroResposta;
import com.bikeunirio.bicicletario.aluguel.entity.Funcionario;
import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/funcionario")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;
    
    @GetMapping
    public ResponseEntity<List<Funcionario>> getAllFuncionarios() {
        List<Funcionario> funcionarios = funcionarioService.getAllFuncionarios();
        return ResponseEntity.ok(funcionarios);
    }

    @PostMapping
    public ResponseEntity<Funcionario> createFuncionario(@Valid @RequestBody Funcionario funcionario) {
        Funcionario novoFuncionario = funcionarioService.createFuncionario(funcionario);
        return ResponseEntity.ok(novoFuncionario);
    }

    
    @GetMapping("/{idFuncionario}")
    public void readFuncionario() {
    	
    }
    
    @PutMapping("/{idFuncionario}")
    public void updateFuncionario() {
    	
    }
    
    @DeleteMapping("/{idFuncionario}")
    public void deleteFuncionario() {
    	
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ErroResposta>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(
                    ex.getBindingResult()
                      .getFieldErrors()
                      .stream()
                      .map(e -> new ErroResposta("DADOS_INVALIDOS", e.getDefaultMessage()))
                      .toList()
                );
    }

}