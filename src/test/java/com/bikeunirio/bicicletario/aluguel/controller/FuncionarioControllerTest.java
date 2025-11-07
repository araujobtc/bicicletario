package com.bikeunirio.bicicletario.aluguel.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.bikeunirio.bicicletario.aluguel.service.FuncionarioService;

@WebMvcTest(FuncionarioController.class)
public class FuncionarioControllerTest {

    @Autowired
    private MockMvc mockMvc; // Simula as requisições HTTP

    @MockBean
    private FuncionarioService funcionarioService; // Mocka o Service

}