package br.com.zupacademy.propostas;

import br.com.zupacademy.propostas.seguranca.DadosSensiveisCrypto;
import br.com.zupacademy.propostas.seguranca.DadosSensiveisOfuscar;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class PropostasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropostasApplication.class, args);
	}

}
