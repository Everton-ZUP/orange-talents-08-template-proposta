package br.com.zupacademy.propostas.proposta.consulta;

import br.com.zupacademy.propostas.proposta.EstadoProposta;
import br.com.zupacademy.propostas.proposta.Proposta;

import java.math.BigDecimal;

public class PropostaConsultaResponse {

    private String documento;
    private String email;
    private String nome;
    private String endereco;
    private BigDecimal salario;
    private EstadoProposta estado;
    private String cartao;

    public PropostaConsultaResponse(Proposta proposta) {
        this.documento = proposta.getDocumento();
        this.email = proposta.getEmail();
        this.nome = proposta.getNome();
        this.endereco = proposta.getEndereco();
        this.salario = proposta.getSalario();
        this.estado = proposta.getEstado();

        if (proposta.getCartao() != null)
            this.cartao = proposta.getCartao().getNumeroCartao();
    }

    public String getDocumento() {
        return documento;
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

    public BigDecimal getSalario() {
        return salario;
    }

    public EstadoProposta getEstado() {
        return estado;
    }

    public String getCartao() {
        return cartao;
    }
}
