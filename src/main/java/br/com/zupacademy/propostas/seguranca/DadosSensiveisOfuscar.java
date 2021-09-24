package br.com.zupacademy.propostas.seguranca;

import java.nio.charset.StandardCharsets;

public class DadosSensiveisOfuscar {

    public static String ofuscar(String info){
        char[] letras = info.toCharArray();
        String texto = "";
        for (int i = 0; i <= letras.length-1; i++){
            if (i % 2 == 0){
                texto += letras[i];
            }else{
                texto += "*";
            }
        }
        return texto;
    }
}
