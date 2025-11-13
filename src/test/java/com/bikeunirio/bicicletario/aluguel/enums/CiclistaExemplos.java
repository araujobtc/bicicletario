package com.bikeunirio.bicicletario.aluguel.enums;

import java.time.LocalDate;

import org.springframework.beans.BeanUtils;

import com.bikeunirio.bicicletario.aluguel.dto.CiclistaDTO;
import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;
import com.bikeunirio.bicicletario.aluguel.dto.PassaporteDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Cartao;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Passaporte;

public class CiclistaExemplos {

    public static final Ciclista CICLISTA;
    public static final Passaporte PASSAPORTE;
    public static final Cartao CARTAO;
    
    public static final CiclistaDTO CICLISTA_DTO;
    public static final PassaporteDTO PASSAPORTE_DTO;
    public static final MeioDePagamentoDTO MEIOPAGAMENTO_DTO;
    
    static {
    	PASSAPORTE = new Passaporte();
    	PASSAPORTE.setNumero("XPTO12345");
    	PASSAPORTE.setValidade(LocalDate.of(2030, 5, 10));
    	PASSAPORTE.setPais("BR");
    	
        PASSAPORTE_DTO = new PassaporteDTO();
		BeanUtils.copyProperties(PASSAPORTE, PASSAPORTE_DTO, "id");
    }
    
    static {
        CARTAO = new Cartao();
        CARTAO.setNomeTitular("Isabelle Araujo");
        CARTAO.setNumero("1234567812345678");
        CARTAO.setValidade(LocalDate.of(2030, 12, 31));
        CARTAO.setCvv("123");

        MEIOPAGAMENTO_DTO = new MeioDePagamentoDTO();
		BeanUtils.copyProperties(CARTAO, MEIOPAGAMENTO_DTO, "id");
    }
    
    static {
        CICLISTA = new Ciclista();
        CICLISTA.setNome("Isabelle Araujo");
        CICLISTA.setNascimento(LocalDate.of(2000, 1, 1));
        CICLISTA.setCpf("12345678901");
        CICLISTA.setNacionalidade(Nacionalidades.BRASILEIRO);
        CICLISTA.setEmail("isa@exemplo.com");
        CICLISTA.setUrlFotoDocumento("http://exemplo.com/doc.jpg");
        CICLISTA.setSenha("senha123");
        CICLISTA.setPassaporte(PASSAPORTE);
        CICLISTA.setCartao(CARTAO);

        CARTAO.setCiclista(CICLISTA);

        CICLISTA_DTO = new CiclistaDTO();

		BeanUtils.copyProperties(CICLISTA, CICLISTA_DTO, "id", "passaporte", "meioDePagamento");
		
        CICLISTA_DTO.setPassaporte(PASSAPORTE_DTO);
        CICLISTA_DTO.setMeioDePagamento(MEIOPAGAMENTO_DTO);
        
        
    }
    
}

