package br.com.zupacademy.propostas.cartao.carteira;

import br.com.zupacademy.propostas.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class CarteiraDigitalPaypal {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String idAssociacao;

    @NotBlank @Email
    private String email;

    @OneToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    public CarteiraDigitalPaypal(String idAssociacao, String email, Cartao cartao) {
        this.idAssociacao = idAssociacao;
        this.email = email;
        this.cartao = cartao;
    }

    @Deprecated
    public CarteiraDigitalPaypal() {
    }

    public Long getId() {
        return id;
    }

    public String getIdAssociacao() {
        return idAssociacao;
    }

    public String getEmail() {
        return email;
    }

    public Cartao getCartao() {
        return cartao;
    }
}
