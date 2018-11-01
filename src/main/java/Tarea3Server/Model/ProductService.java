package Tarea3Server.Model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import Tarea3Server.Repositories.ProductRepository;
import java.util.List;
import java.util.Objects;

@Service
public class ProductService implements ProductList {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getProductList(){
        return productRepository.findAll();
    }
    public void addProduct(Product product){
        productRepository.save(product);
    }
    public Product getProduct(String code){
        return productRepository.findByCode(code);
    }
    public boolean removeProduct(String code){
        Product product = this.getProduct(code);
        if(Objects.isNull(product)){
            return false;
        }
        else{
            productRepository.removeByCode(code);
            return true;
        }
    }
}
