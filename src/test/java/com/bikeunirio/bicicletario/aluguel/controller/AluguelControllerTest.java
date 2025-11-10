package com.bikeunirio.bicicletario.aluguel.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.service.AluguelService;

@ExtendWith(MockitoExtension.class)
public class AluguelControllerTest {

    @InjectMocks
    private AluguelController controller;

    @Mock
    private AluguelService service; // Mocka o Service
	
}
