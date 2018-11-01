package Tarea3Server.Components;

import Tarea3Server.Model.Product;
import Tarea3Server.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;


@Component
public class DatabaseLoader {
    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    private void initDatabase(){

        productRepository.save(
                new Product("001ABC","Coca-cola","Bebida",51)
        );

        productRepository.save(
                new Product("123123","Fanta","Bebida", 45)
        );

        List<Product> productList = productRepository.findAll();
        productList.stream().forEach(product -> System.out.println("Code: " + product.getCode()));
    }

}

