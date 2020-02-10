package cl.fonasa.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"cl.fonasa"})
public class SpringBootUploadFile {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootUploadFile.class, args);
	}
}
