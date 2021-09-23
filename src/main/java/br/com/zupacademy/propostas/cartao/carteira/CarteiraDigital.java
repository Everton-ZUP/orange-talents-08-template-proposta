package br.com.zupacademy.propostas.cartao.carteira;

import br.com.zupacademy.propostas.cartao.Cartao;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import java.util.Objects;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class CarteiraDigital {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String idAssociacao;

    @NotBlank @Email
    private String email;

    @OneToOne
    @JoinColumn(name = "cartao_id")
    private Cartao cartao;

    @Enumerated(EnumType.STRING)
    private TipoCarteira tipoCarteira;

    public CarteiraDigital(String idAssociacao, String email, Cartao cartao, TipoCarteira tipo) {
        this.idAssociacao = idAssociacao;
        this.email = email;
        this.cartao = cartao;
        this.tipoCarteira = tipo;
    }

    @Deprecated
    public CarteiraDigital() {
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

    public TipoCarteira getTipoCarteira() {
        return tipoCarteira;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarteiraDigital that = (CarteiraDigital) o;
        return Objects.equals(cartao, that.cartao) && tipoCarteira == that.tipoCarteira;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cartao, tipoCarteira);
    }
}
