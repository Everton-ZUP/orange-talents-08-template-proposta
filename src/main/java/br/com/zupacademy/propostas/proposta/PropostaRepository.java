package br.com.zupacademy.propostas.proposta;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;

public interface PropostaRepository extends JpaRepository<Proposta,Long> {

   Proposta findByDocumento(String s);

    List<Proposta> findAllByStatusAndCartaoIsNull(EstadoProposta elegivel);
}
