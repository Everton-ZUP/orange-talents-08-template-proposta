package br.com.zupacademy.propostas.cartao;

import br.com.zupacademy.propostas.biometria.Biometria;
import br.com.zupacademy.propostas.cartao.bloqueio.BloqueioCartao;
import br.com.zupacademy.propostas.cartao.viagem.AvisoViagem;
import br.com.zupacademy.propostas.cartao.vinculado.Vencimento;
import br.com.zupacademy.propostas.proposta.Proposta;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


@Entity
public class Cartao {

    @NotNull @Column(unique = true)
    private String numeroCartao;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String uuid;

    private LocalDateTime emitidoEm;
    private String titular;

    @Enumerated(EnumType.STRING)
    private StatusCartao status;

    @OneToOne(mappedBy = "cartao")
    private Proposta proposta;

    @OneToMany(mappedBy = "cartao", cascade = CascadeType.MERGE)
    private List<Biometria> biometrias = new ArrayList<>();

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "bloqueio_id")
    private BloqueioCartao bloqueio;

    private BigDecimal limite;

    @OneToMany(mappedBy = "cartao")
    private List<AvisoViagem> avisoViagem = new ArrayList<>();

    @ElementCollection
    private List<HashMap<String,Object>> bloqueios;
    @ElementCollection
    private List<HashMap<String,Object>> avisos;
    @ElementCollection
    private List<HashMap<String,Object>> carteiras;
    @ElementCollection
    private List<HashMap<String,Object>> parcelas;

    private HashMap<String,Object> renegociacao;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "vencimento_id")
    private Vencimento vencimento;

    public Cartao(String numeroCartao, LocalDateTime emitidoEm, String titular, Proposta proposta, BigDecimal limite,
                  List<HashMap<String, Object>> bloqueios, List<HashMap<String, Object>> avisos, List<HashMap<String, Object>> carteiras,
                  List<HashMap<String, Object>> parcelas, HashMap<String, Object> renegociacao, Vencimento vencimento) {
        this.numeroCartao = numeroCartao;
        this.emitidoEm = emitidoEm;
        this.titular = titular;
        this.proposta = proposta;
        this.limite = limite;
        this.bloqueios = bloqueios;
        this.avisos = avisos;
        this.carteiras = carteiras;
        this.parcelas = parcelas;
        this.renegociacao = renegociacao;
        this.vencimento = vencimento;
    }

    @Deprecated
    public Cartao() {
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public LocalDateTime getEmitidoEm() {
        return emitidoEm;
    }

    public String getTitular() {
        return titular;
    }

    public Proposta getProposta() {
        return proposta;
    }

    public BigDecimal getLimite() {
        return limite;
    }

    public List<HashMap<String, Object>> getBloqueios() {
        return bloqueios;
    }

    public List<HashMap<String, Object>> getAvisos() {
        return avisos;
    }

    public List<HashMap<String, Object>> getCarteiras() {
        return carteiras;
    }

    public List<HashMap<String, Object>> getParcelas() {
        return parcelas;
    }

    public HashMap<String, Object> getRenegociacao() {
        return renegociacao;
    }

    public Vencimento getVencimento() {
        return vencimento;
    }

    public BloqueioCartao getBloqueio() {
        return bloqueio;
    }

    public boolean cartaoBloqueado() {
        return bloqueio != null;
    }

    public List<Biometria> getBiometrias() {
        return biometrias;
    }

    public void adicionarBiometria(Biometria biometria){
        this.biometrias.add(biometria);
    }

    public StatusCartao getStatus() {
        return status;
    }

    public void setStatus(StatusCartao status) {
        this.status = status;
    }

    public String getUuid() {
        return uuid;
    }

    public List<AvisoViagem> getAvisoViagem() {
        return avisoViagem;
    }

    public void adicionaAvisoViagem(AvisoViagem avisoViagem) {
        this.avisoViagem.add(avisoViagem);
    }
}
