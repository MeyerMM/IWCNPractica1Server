package tarea.server.repositories;

import tarea.server.model.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.math.BigDecimal;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepository extends CrudRepository<Product, String>{
    List<Product> findAll();

    Product findByCode(String code);

    @Transactional
    Product save(Product product);

    @Transactional
    void removeByCode(String code);

    @Transactional
    @Modifying
    @Query("update Product p set p.name = ?2 where p.code = ?1")
    void setProductNameByCode(String code,  String name);

    @Transactional
    @Modifying
    @Query("update Product p set p.description = ?2 where p.code = ?1")
    void setProductDescriptionByCode(String code,  String description);

    @Transactional
    @Modifying
    @Query("update Product p set p.price = ?2 where p.code = ?1")
    void setProductPriceByCode(String code,  BigDecimal price);
}

