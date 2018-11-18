package tarea.server.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tarea.server.repositories.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.math.BigDecimal;

@Service
public class ProductService implements ProductList {
    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }
    public List<Product> getProductList(){
        return productRepository.findAll();
    }
    public boolean addProduct(Product product){
        Product addedProduct = productRepository.save(product);
        return addedProduct != null;
    }
    public void updateProduct(String code, String name, String description, BigDecimal price){
        if(code != null && !code.isEmpty()){
            if(name != null && !name.isEmpty()){
                productRepository.setProductNameByCode(code, name);
            }
            if(description != null && !description.isEmpty()){
                productRepository.setProductDescriptionByCode(code, description);
            }
            if(price != null){
                productRepository.setProductPriceByCode(code, price);
            }
        }
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
