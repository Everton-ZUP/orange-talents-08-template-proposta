package br.com.zupacademy.propostas.seguranca;


import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class DadosSensiveisCrypto {

    private static TextEncryptor textEncryptor =
            Encryptors.queryableText("criptografia","A307F432A855C3122522");

    public static String encrypt(String info){
        if (info == null){
            return "";
        }
        return textEncryptor.encrypt(info);
    }
    public static String decrypt(String info){
        if (info == null){
            return "";
        }
        return textEncryptor.decrypt(info);
    }
}
