package br.com.zupacademy.propostas.cartao.viagem;

import br.com.zupacademy.propostas.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class AvisoViagem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull @NotBlank
    private String destino;
    private String ip;
    private String userAgent;

    @NotNull @Future
    private LocalDate terminoViagem;
    private LocalDateTime instanteDeCriacao = LocalDateTime.now();

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;


    public AvisoViagem(String destino, String ip, String userAgent, LocalDate terminoViagem, Cartao cartao) {
        this.destino = destino;
        this.ip = ip;
        this.userAgent = userAgent;
        this.terminoViagem = terminoViagem;
        this.cartao = cartao;
    }

    @Deprecated
    public AvisoViagem() {
    }

    public Long getId() {
        return id;
    }

    public String getDestino() {
        return destino;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public LocalDate getTerminoViagem() {
        return terminoViagem;
    }

    public LocalDateTime getInstanteDeCriacao() {
        return instanteDeCriacao;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
