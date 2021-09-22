package br.com.zupacademy.propostas.cartao.viagem;

public enum AvisoViagemRetornoEnum {
    CRIADO{
        @Override
        boolean sucessoOuFalha() {
            return true;
        }
    },
    FALHA{
        @Override
        boolean sucessoOuFalha() {
            return false;
        }
    };

   abstract boolean sucessoOuFalha();
}
