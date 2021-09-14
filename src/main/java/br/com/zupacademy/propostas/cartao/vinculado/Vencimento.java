package br.com.zupacademy.propostas.cartao.vinculado;

import br.com.zupacademy.propostas.cartao.Cartao;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class Vencimento {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long idLocal;

    private String id;
    private Integer dia;
    private LocalDateTime dataDeCriacao;

    @OneToOne(mappedBy = "vencimento")
    private Cartao cartao;

    public Vencimento(String id, Integer dia, LocalDateTime dataDeCriacao) {
        this.id = id;
        this.dia = dia;
        this.dataDeCriacao = dataDeCriacao;
    }

    @Deprecated
    public Vencimento() {
    }

    public Long getIdLocal() {
        return idLocal;
    }

    public String getId() {
        return id;
    }

    public Integer getDia() {
        return dia;
    }

    public LocalDateTime getDataDeCriacao() {
        return dataDeCriacao;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
