package com.bikeunirio.bicicletario.aluguel.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.service.CiclistaService;

@ExtendWith(MockitoExtension.class)
public class CiclistaControllerTest {

    @InjectMocks
    private CiclistaController controller;

    @Mock
    private CiclistaService service; // Mocka o Service

}
