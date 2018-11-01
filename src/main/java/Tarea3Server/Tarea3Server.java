package Tarea3Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class Tarea3Server {
    public static void main(String[] args) {
        SpringApplication.run(Tarea3Server.class, args);
    }
}