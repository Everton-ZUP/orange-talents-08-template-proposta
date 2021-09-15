package br.com.zupacademy.propostas.biometria;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BiometriaRepository extends JpaRepository<Biometria, UUID> {

}
