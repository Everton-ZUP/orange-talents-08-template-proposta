package br.com.zupacademy.propostas.cartao.bloqueio;

import br.com.zupacademy.propostas.cartao.Cartao;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class BloqueioCartao {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private LocalDateTime instanteBloqueio = LocalDateTime.now();
    private String ip;
    private String userAgent;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @Deprecated
    public BloqueioCartao() {
    }

    public BloqueioCartao(String ip, String userAgent, Cartao cartao) {
        this.ip = ip;
        this.userAgent = userAgent;
        this.cartao = cartao;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getInstanteBloqueio() {
        return instanteBloqueio;
    }

    public String getIp() {
        return ip;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
