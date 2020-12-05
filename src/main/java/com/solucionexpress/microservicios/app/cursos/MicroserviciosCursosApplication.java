package com.solucionexpress.microservicios.app.cursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
@EntityScan({"com.solucionexpress.microservicios.commons.alumnos.models.entity",
			 "com.solucionexpress.microservicios.commons.examenes.models.entity",
		 	 "com.solucionexpress.microservicios.app.cursos.models.entity"})
public class MicroserviciosCursosApplication {

    public static void main(String[] args) {
        SpringApplication.run(MicroserviciosCursosApplication.class, args);
    }

}
