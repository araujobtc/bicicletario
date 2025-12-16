package com.bikeunirio.bicicletario.aluguel.webservice;

import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;

@Service
public class EquipamentosService {

    private static final String BASE_URL = "https://bicicletarioequipamentos.onrender.com";

    private final RestTemplate restTemplate;

    public EquipamentosService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // GET /bicicleta/{idBicicleta}
    public Optional<BicicletaDTO> getBicicletaPorId(Long idBicicleta) {

        String url = BASE_URL + "/bicicleta/" + idBicicleta;

        try {
            ResponseEntity<BicicletaDTO> response = restTemplate.getForEntity(url, BicicletaDTO.class);

            return Optional.ofNullable(response.getBody());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // GET /tranca/{idTranca}/bicicleta
    public Optional<Long> getBicicletaPorIdTranca(Long idTranca) {

        String url = BASE_URL + "/tranca/" + idTranca + "/bicicleta";

        try {
            ResponseEntity<BicicletaDTO> response = restTemplate.getForEntity(url, BicicletaDTO.class);

            return Optional.ofNullable(response.getBody().getId());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // POST /tranca/{idTranca}/status/TRANCAR
    public boolean atualizarStatusTranca(Long idTranca) {

        String url = BASE_URL + "/tranca/" + idTranca + "/status/TRANCAR";

        try {
            restTemplate.postForEntity(url, null, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // POST /bicicleta/{idBicicleta}/status/{acao}
    public boolean atualizarStatusBicicleta(Long idBicicleta, String status) {

        String url = BASE_URL + "/bicicleta/" + idBicicleta + "/status/" + status;

        try {
            restTemplate.postForEntity(url, null, Void.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // GET /tranca/{idTranca}
    public boolean isTrancaDisponivel(Long idTranca) {

        String url = BASE_URL + "/tranca/" + idTranca;

        try {
            restTemplate.getForEntity(url, Object.class);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
