package br.com.zupacademy.propostas.proposta.avaliacao;

import br.com.zupacademy.propostas.proposta.EstadoProposta;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ResponseAvaliacaoFinanceira {

    @JsonProperty
    private String documento;
    @JsonProperty
    private String nome;
    @JsonProperty
    private EnumAvaliacaoFinanceiraResultado resultadoSolicitacao;
    @JsonProperty
    private String idProposta;

    public ResponseAvaliacaoFinanceira(String documento, String nome, EnumAvaliacaoFinanceiraResultado resultadoSolicitacao, String idProposta) {
        this.documento = documento;
        this.nome = nome;
        this.resultadoSolicitacao = resultadoSolicitacao;
        this.idProposta = idProposta;
    }

    public ResponseAvaliacaoFinanceira() {
    }

    public String getDocumento() {
        return documento;
    }

    public String getNome() {
        return nome;
    }

    public String getIdProposta() {
        return idProposta;
    }

    public EnumAvaliacaoFinanceiraResultado getResultadoSolicitacao() {
        return resultadoSolicitacao;
    }

    @JsonIgnore
    public EstadoProposta getStatusProposta() {
        return this.resultadoSolicitacao.statusProposta();
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setResultadoSolicitacao(EnumAvaliacaoFinanceiraResultado resultadoSolicitacao) {
        this.resultadoSolicitacao = resultadoSolicitacao;
    }

    public void setIdProposta(String idProposta) {
        this.idProposta = idProposta;
    }
}
