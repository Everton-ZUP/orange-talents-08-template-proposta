package br.com.zupacademy.propostas.proposta;

import br.com.zupacademy.propostas.cartao.Cartao;
import br.com.zupacademy.propostas.seguranca.DadosSensiveisCrypto;
import br.com.zupacademy.propostas.validation.CpfOuCnpj;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Entity
public class Proposta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(unique = true)
    private String documento;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotNull @Positive
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private EstadoProposta estado;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "numero_cartao")
    private Cartao cartao;

    /**
     *
     * @param documento Deve ser sem criptografia
     * @param email
     * @param nome
     * @param endereco
     * @param salario
     */
    public Proposta(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = DadosSensiveisCrypto.encrypt(documento);
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    @Deprecated
    public Proposta() {
    }

    public Long getId() {
        return id;
    }

    public String getDocumento() {
        return DadosSensiveisCrypto.decrypt(documento);
    }

    public String getEmail() {
        return email;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public EstadoProposta getEstado() {
        return estado;
    }

    public BigDecimal getSalario() {
        return salario;
    }

    public void setEstado(EstadoProposta estado) {
        this.estado = estado;
    }

    public Cartao getCartao() {
        return cartao;
    }

    public void setCartao(Cartao cartao) {
        this.cartao = cartao;
    }
}
