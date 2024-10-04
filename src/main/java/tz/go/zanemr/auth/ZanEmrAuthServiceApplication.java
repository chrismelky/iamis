package tz.go.zanemr.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ZanEmrAuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZanEmrAuthServiceApplication.class, args);
	}

}
