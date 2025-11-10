package com.bikeunirio.bicicletario.aluguel.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;

@ExtendWith(MockitoExtension.class)
public class AluguelServiceTest {

    @InjectMocks
    private AluguelService service;
    
    @Mock
    private AluguelRepository repository; // Mocka o Repository

}
