package com.bikeunirio.bicicletario.aluguel.webservice;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;

@Service
public class EquipamentosService {

    private static final String BASE_URL = "https://bicicletarioequipamentos.onrender.com";
    private static final String TRANCA_URL = BASE_URL + "/tranca/";
    private static final String BICICLETA_URL = BASE_URL + "/bicicleta/";

    private final RestTemplate restTemplate;

    public EquipamentosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<BicicletaDTO> getBicicletaPorId(Long idBicicleta) {
        try {
            String url = BICICLETA_URL + idBicicleta;

            ResponseEntity<BicicletaDTO> response = restTemplate.getForEntity(url, BicicletaDTO.class);

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<BicicletaDTO> getBicicletaPorIdTranca(Long idTranca) {
        try {
            String url = TRANCA_URL + idTranca + "/bicicleta";

            ResponseEntity<BicicletaDTO> response = restTemplate.getForEntity(url, BicicletaDTO.class);

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public boolean atualizarStatusTranca(Long idTranca) {
        try {
            String url = TRANCA_URL + idTranca + "/status/TRANCAR";

            restTemplate.postForEntity(url, null, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean atualizarStatusBicicleta(Long idBicicleta, String status) {
        try {
            String url = BICICLETA_URL + idBicicleta + "/status/" + status;

            restTemplate.postForEntity(url, null, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTrancaDisponivel(Long idTranca) {
        try {
            String url = TRANCA_URL + idTranca;

            restTemplate.getForEntity(url, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
