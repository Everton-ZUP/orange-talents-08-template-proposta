package br.com.zupacademy.propostas.proposta.cadastro;

import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.seguranca.DadosSensiveisCrypto;
import br.com.zupacademy.propostas.validation.CpfOuCnpj;
import br.com.zupacademy.propostas.validation.UniqueValue;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class PropostaRequest {

    @NotBlank
    @CpfOuCnpj
    @UniqueValue(classe = Proposta.class, campo = "documento")
    private String documento;
    @Email @NotBlank
    private String email;
    @NotBlank
    private String nome;
    @NotBlank
    private String endereco;
    @NotNull @Positive
    private BigDecimal salario;

    public PropostaRequest(String documento, String email, String nome, String endereco, BigDecimal salario) {
        this.documento = documento;
        this.email = email;
        this.nome = nome;
        this.endereco = endereco;
        this.salario = salario;
    }

    public Proposta toModel(){
        return new Proposta(this.documento,this.email,this.nome,this.endereco,this.salario);
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
}
