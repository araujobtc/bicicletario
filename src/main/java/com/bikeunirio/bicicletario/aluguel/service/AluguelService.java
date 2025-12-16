package com.bikeunirio.bicicletario.aluguel.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.bikeunirio.bicicletario.aluguel.dto.BicicletaDTO;
import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;
import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;
import com.bikeunirio.bicicletario.aluguel.entity.Devolucao;
import com.bikeunirio.bicicletario.aluguel.repository.AluguelRepository;
import com.bikeunirio.bicicletario.aluguel.repository.DevolucaoRepository;
import com.bikeunirio.bicicletario.aluguel.webservice.EquipamentosService;
import com.bikeunirio.bicicletario.aluguel.webservice.ExternoService;

@Service
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final DevolucaoRepository devolucaoRepository;
    private final EquipamentosService equipamentosService;
    private final ExternoService externoService;
    private final CiclistaService ciclistaService;

    public AluguelService(
            AluguelRepository aluguelRepository,
            DevolucaoRepository devolucaoRepository,
            CiclistaService ciclistaService,
            ExternoService externoService,
            EquipamentosService equipamentosService) {

        this.aluguelRepository = aluguelRepository;
        this.devolucaoRepository = devolucaoRepository;
        this.ciclistaService = ciclistaService;
        this.externoService = externoService;
        this.equipamentosService = equipamentosService;
    }

    public Optional<BicicletaDTO> getBicicletaPorIdCiclista(Long idCiclista) {
        return aluguelRepository
                .findByCiclistaIdAndHoraFimIsNull(idCiclista)
                .flatMap(aluguel -> equipamentosService.getBicicletaPorId(aluguel.getBicicletaId()));
    }

    public boolean isCiclistaComAluguelAtivo(Long idCiclista) {
        return aluguelRepository
                .findByCiclistaIdAndHoraFimIsNull(idCiclista)
                .isPresent();
    }

    public Optional<Aluguel> alugar(Long trancaInicio, Ciclista ciclista) {

        Optional<BicicletaDTO> bicicletaOpt = equipamentosService.getBicicletaPorIdTranca(trancaInicio);

        if (bicicletaOpt.isEmpty()) {
            return Optional.empty();
        }

        BicicletaDTO bicicleta = bicicletaOpt.get();

        Long cobrancaId = externoService.realizarCobranca(ciclista.getId(), 10.0);
        if (cobrancaId == null) {
            return Optional.empty();
        }

        Aluguel aluguel = new Aluguel();
        aluguel.setCiclista(ciclista);
        aluguel.setBicicletaId(bicicleta.getId());
        aluguel.setTrancaInicio(trancaInicio);
        aluguel.setHoraInicio(LocalDateTime.now());
        aluguel.setCobranca(cobrancaId);

        aluguelRepository.save(aluguel);

        equipamentosService.atualizarStatusBicicleta(bicicleta.getId(), "EM_USO");
        equipamentosService.atualizarStatusTranca(trancaInicio);

        externoService.enviarEmail(
                ciclista.getEmail(),
                "Bicicleta alugada com sucesso. ID: " + bicicleta);

        return Optional.of(aluguel);
    }

    public Optional<Devolucao> devolver(Long idBicicleta, Long idTranca) {

        Optional<Aluguel> aluguelOpt = aluguelRepository.findByBicicletaIdAndHoraFimIsNull(idBicicleta);

        if (aluguelOpt.isEmpty()) {
            return Optional.empty();
        }

        Aluguel aluguel = aluguelOpt.get();

        LocalDateTime agora = LocalDateTime.now();
        aluguel.setHoraFim(agora);
        aluguel.setTrancaFim(idTranca);

        aluguelRepository.save(aluguel);

        Devolucao devolucao = new Devolucao();
        BeanUtils.copyProperties(aluguel, devolucao, "id", "cobranca");

        long minutosUso = java.time.Duration.between(aluguel.getHoraInicio(), agora).toMinutes();

        if (minutosUso > 120) {
            long meiaHorasExtras = (minutosUso - 120) / 30;
            double valorExtra = meiaHorasExtras * 5.0;

            Long cobrancaExtra = externoService.realizarCobranca(aluguel.getCiclista(), valorExtra);

            devolucao.setCobranca(cobrancaExtra);
        }

        devolucaoRepository.save(devolucao);

        equipamentosService.atualizarStatusBicicleta(idBicicleta, "DISPONIVEL");
        equipamentosService.atualizarStatusTranca(idTranca);

        ciclistaService.readCiclista(aluguel.getCiclista())
                .ifPresent(c -> externoService.enviarEmail(
                        c.getEmail(),
                        "Bicicleta devolvida com sucesso"));

        return Optional.of(devolucao);
    }
}
