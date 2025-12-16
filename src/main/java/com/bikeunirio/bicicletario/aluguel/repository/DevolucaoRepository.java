package com.bikeunirio.bicicletario.aluguel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bikeunirio.bicicletario.aluguel.entity.Devolucao;

@Repository
public interface DevolucaoRepository extends JpaRepository<Devolucao, Long> {

}
