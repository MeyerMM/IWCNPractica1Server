package tarea.server.components;

import tarea.server.model.Product;
import tarea.server.model.User;
import tarea.server.repositories.ProductRepository;
import tarea.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;


@Component
public class DatabaseLoader {
    private ProductRepository productRepository;
    private UserRepository userRepository;

    @Autowired
    public DatabaseLoader(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    public void initDatabase(){
        List<String> adminRoles = Arrays.asList("ROLE_VIEW", "ROLE_ADMIN");
        List<String> userRoles = Arrays.asList("ROLE_VIEW");
        String encodedPassword = "$2a$04$DP1EadCzK7tDCspks.B4MO0iOE2hUoPY3AVelG0kjALRcoLU52qZG"; // Password 1234 encoded with BCrypt and a strength of 4
        userRepository.save(new User("root", encodedPassword, adminRoles));
        userRepository.save(new User("user", encodedPassword, userRoles));
    }

}

