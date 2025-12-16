package com.bikeunirio.bicicletario.aluguel.webservice;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bikeunirio.bicicletario.aluguel.dto.MeioDePagamentoDTO;

@Service
public class ExternoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternoService.class);
    private static final String BASE_URL = "https://bicicletarioexterno.onrender.com";

    private final RestTemplate restTemplate;

    public ExternoService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // POST /enviarEmail
    public boolean enviarEmail(String emailDestinatario, String conteudo) {

        String url = BASE_URL + "/enviarEmail";

        Map<String, Object> body = new HashMap<>();
        body.put("email", emailDestinatario);
        body.put("mensagem", conteudo);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            LOGGER.info("Email enviado com sucesso para {}", emailDestinatario);
            return response.getStatusCode().is2xxSuccessful();

        } catch (Exception e) {
            LOGGER.error("Erro ao enviar email", e);
            return false;
        }
    }

    // POST /validaCartaoDeCredito
    public boolean isCartaoInvalido(MeioDePagamentoDTO cartao) {

        String url = BASE_URL + "/validaCartaoDeCredito";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<MeioDePagamentoDTO> request = new HttpEntity<>(cartao, headers);

        try {
            restTemplate.postForEntity(url, request, Void.class);
            return false; // cartão válido
        } catch (Exception e) {
            LOGGER.warn("Cartão inválido");
            return true;
        }
    }

    // POST /cobranca
    @SuppressWarnings("rawtypes")
    public Long realizarCobranca(Long ciclistaId, double valor) {

        String url = BASE_URL + "/cobranca";

        Map<String, Object> body = new HashMap<>();
        body.put("ciclista", ciclistaId);
        body.put("valor", valor);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        return ((Number) response.getBody().get("id")).longValue();
    }
}
