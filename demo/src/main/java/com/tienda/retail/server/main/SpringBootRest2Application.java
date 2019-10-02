package com.tienda.retail.server.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.tienda.retail.controllers.ProductRegistrationController;
import com.tienda.retail.model.ProductosDetalle;



@SpringBootApplication(scanBasePackages = {"com.tienda.retail"})
@EntityScan("com.tienda.retail.model")

public class SpringBootRest2Application  {

	public static void main(String[] args) {
		   SpringApplication.run(SpringBootRest2Application.class, args);
	}

}
