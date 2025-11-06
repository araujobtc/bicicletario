package com.bikeunirio.bicicletario.aluguel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bikeunirio.bicicletario.aluguel.entity.Aluguel;

@Repository
public interface AluguelRepository extends JpaRepository<Aluguel, Long> {
	
}
