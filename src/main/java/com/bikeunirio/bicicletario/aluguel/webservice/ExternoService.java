package com.bikeunirio.bicicletario.aluguel.webservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;

@Service
public class ExternoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternoService.class);

    public boolean enviarEmail(String emailDestinatario, String conteudo) {
        LOGGER.info("📧 Tentativa de envio de e-mail para: {}. Conteúdo: {}", emailDestinatario, conteudo);
        return true; 
    }

    public boolean isCartaoInvalido(MeioDePagamentoDTO cartao) {
        LOGGER.debug("Iniciando validação de cartão do titular: {}", cartao.getNomeTitular());
        if ("Titular Invalido".equalsIgnoreCase(cartao.getNomeTitular())) {
             LOGGER.warn("Validação de cartão FALHOU: Cartão marcado como inválido para o titular.");
             return true; 
        }

        LOGGER.debug("Validação de cartão concluída. Cartão considerado válido.");
        return false;
    }

	public Long realizarCobranca(Long id, double valor) {
        LOGGER.info("📧 id: {}. valor: {}", id, valor);
		return 1L;
	}

	public Long cobrar(Long ciclista, double valorExtra) {
		return (long) (ciclista + valorExtra);
	}
}