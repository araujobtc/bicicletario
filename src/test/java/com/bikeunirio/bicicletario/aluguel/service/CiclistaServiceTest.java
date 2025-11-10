package com.bikeunirio.bicicletario.aluguel.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bikeunirio.bicicletario.aluguel.repository.CiclistaRepository;

@ExtendWith(MockitoExtension.class)
public class CiclistaServiceTest {

    @InjectMocks
    private CiclistaService service;
    
    @Mock
    private CiclistaRepository repository; // Mocka o Repository

}
