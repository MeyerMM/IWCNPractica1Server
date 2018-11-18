package tarea.server.model;

import java.util.List;
import java.math.BigDecimal;

public interface ProductList {
    List<Product> getProductList();
    boolean addProduct(Product product);
    void updateProduct(String code, String name, String description, BigDecimal price);
    Product getProduct(String code);
    boolean removeProduct(String code);

}
