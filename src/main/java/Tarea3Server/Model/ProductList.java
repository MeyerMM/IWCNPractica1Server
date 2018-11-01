package Tarea3Server.Model;

import java.util.List;

public interface ProductList {
    List<Product> getProductList();
    void addProduct(Product product);
    Product getProduct(String code);
    boolean removeProduct(String code);
}
