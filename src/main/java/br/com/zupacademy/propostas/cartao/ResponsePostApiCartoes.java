package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.cartao.vinculado.Vencimento;
import br.com.zupacademy.propostas.proposta.Proposta;
import br.com.zupacademy.propostas.proposta.PropostaRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponsePostApiCartoes {

    private String id;
    private LocalDateTime emitidoEm;
    private String titular;
    private String idProposta;
    private BigDecimal limite;

    private List<HashMap<String,Object>> bloqueios;
    private List<HashMap<String,Object>> avisos;
    private List<HashMap<String,Object>> carteiras;
    private List<HashMap<String,Object>> parcelas;
    private HashMap<String,Object> renegociacao;
    private Map<String,Object> vencimento;

    public ResponsePostApiCartoes(String id, LocalDateTime emitidoEm, String titular, String idProposta, BigDecimal limite,
                                  List<HashMap<String, Object>> bloqueios, List<HashMap<String, Object>> avisos,
                                  List<HashMap<String, Object>> carteiras, List<HashMap<String, Object>> parcelas,
                                  HashMap<String, Object> renegociacao, Map<String, Object> vencimento) {
        this.id = id;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.idProposta = idProposta;
        this.limite = limite;
        this.bloqueios = bloqueios;
        this.avisos = avisos;
        this.carteiras = carteiras;
        this.parcelas = parcelas;
        this.renegociacao = renegociacao;
        this.vencimento = vencimento;
    }

    public Cartao toCartao() {
        Vencimento venc = null;
        if (!this.vencimento.isEmpty() && this.vencimento != null ){
             venc = new Vencimento(this.vencimento.get("id").toString(),
                    Integer.parseInt(this.vencimento.get("dia").toString()),
                    LocalDateTime.parse(this.vencimento.get("dataDeCriacao").toString()));
        }
        return new Cartao(id,emitidoEm,titular,null,limite,bloqueios,avisos,parcelas,renegociacao,venc);
    }
}
