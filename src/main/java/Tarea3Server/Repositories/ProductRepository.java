package Tarea3Server.Repositories;

import Tarea3Server.Model.Product;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String>{
    List<Product> findAll();
    @Transactional
    Product save(Product product);
    Product findByCode(String code);
    @Transactional
    void removeByCode(String code);
}
