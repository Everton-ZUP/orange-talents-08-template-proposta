package br.com.zupacademy.propostas.biometria;

import br.com.zupacademy.propostas.cartao.Cartao;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Biometria {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    @NotBlank
    @Lob
    private String fingerprint;

    private LocalDateTime dataAssociacao = LocalDateTime.now();

    @NotNull @ManyToOne
    private Cartao cartao;

    @Deprecated
    public Biometria() {
    }

    public Biometria(String fingerprint, Cartao cartao) {
        this.fingerprint = fingerprint;
        this.cartao = cartao;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public LocalDateTime getDataAssociacao() {
        return dataAssociacao;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
