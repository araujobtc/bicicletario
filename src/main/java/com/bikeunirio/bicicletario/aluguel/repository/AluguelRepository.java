package com.bikeunirio.bicicletario.aluguel.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    Optional<Aluguel> findByCiclistaIdAndHoraFimIsNull(Long idCiclista);
    
    Optional<Aluguel> findByBicicletaIdAndHoraFimIsNull(Long bicicletaId);

}
