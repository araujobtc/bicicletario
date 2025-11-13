package com.bikeunirio.bicicletario.aluguel.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bikeunirio.bicicletario.aluguel.entity.Ciclista;

@Repository
public interface CiclistaRepository extends JpaRepository<Ciclista, Long> {
	
	public boolean existsByEmail(String email);

}
