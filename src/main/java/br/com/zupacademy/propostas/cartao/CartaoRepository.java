package br.com.zupacademy.propostas.cartao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartaoRepository extends JpaRepository<Cartao,String> {
    @Query("select c from Cartao c where (c.status <> ?1 or c.status is Null ) and c.bloqueio is not null")
    List<Cartao> buscaCartoesBloqueadosComStatusDesatualizado(StatusCartao statusCartao);

}
