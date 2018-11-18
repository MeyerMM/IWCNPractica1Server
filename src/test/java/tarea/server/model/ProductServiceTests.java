package tarea.server.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tarea.server.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class ProductServiceTests {
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductService productService;
    private Product product1;
    private Product product2;


    @Before
    public void setUp(){
        productService = new ProductService(productRepository);
        product1 = new Product(
                "CODE001",
                "Lata de coca-cola",
                "Lata de coca-cola de 33cl",
                BigDecimal.valueOf(0.51)
        );
        product2 = new Product(
                "CODE002",
                "Huevos de campo",
                "Huevos de campo 12 unidades",
                BigDecimal.valueOf(2.54)
        );
    }

    @Test
    public void getProductList(){
        List<Product> expected = new LinkedList<>();
        expected.add(product1);
        expected.add(product2);
        when(productRepository.findAll()).thenReturn(expected);
        List<Product> result = productService.getProductList();
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    public void addProductOk(){
        boolean expected = true;
        when( productRepository.save(product1)).thenReturn(product1);
        boolean result = productService.addProduct(product1);
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void addProductFail(){
        boolean expected = false;
        when( productRepository.save(product1)).thenReturn(null);
        boolean result = productService.addProduct(product1);
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    public void getProductOk(){
        Product expected = product1;
        when(productRepository.findByCode("CODE001")).thenReturn(expected);
        Product result = productService.getProduct("CODE001");
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).findByCode(any(String.class));
    }

    @Test
    public void getProductFail(){
        Product expected = null;
        when(productRepository.findByCode("CODE005")).thenReturn(expected);
        Product result = productService.getProduct("CODE005");
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).findByCode(any(String.class));
    }

    @Test
    public void removeProductOk(){
        Boolean expected = true;
        when(productRepository.findByCode("CODE001")).thenReturn(product1);
        Boolean result = productService.removeProduct("CODE001");
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).findByCode(any(String.class));
        verify(productRepository, times(1)).removeByCode(any(String.class));
    }

    @Test
    public void removeProductFail(){
        Boolean expected = false;
        when(productRepository.findByCode("CODE005")).thenReturn(null);
        Boolean result = productService.removeProduct("CODE005");
        Assert.assertEquals(result, expected);
        verify(productRepository, times(1)).findByCode(any(String.class));
        verify(productRepository, times(0)).removeByCode(any(String.class));
    }

    @Test
    public void updateProductNameOk(){
        productService.updateProduct(product1.getCode(), product1.getName(), null, null);
        verify(productRepository, times(1)).setProductNameByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductDescriptionByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductPriceByCode(any(String.class), any(BigDecimal.class));
    }

    @Test
    public void updateProductDescriptionOk(){
        productService.updateProduct(product1.getCode(), null, product1.getDescription(), null);
        verify(productRepository, times(0)).setProductNameByCode(any(String.class), any(String.class));
        verify(productRepository, times(1)).setProductDescriptionByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductPriceByCode(any(String.class), any(BigDecimal.class));
    }

    @Test
    public void updateProductPriceOk(){
        productService.updateProduct(product1.getCode(), "", "", product1.getPrice());
        verify(productRepository, times(0)).setProductNameByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductDescriptionByCode(any(String.class), any(String.class));
        verify(productRepository, times(1)).setProductPriceByCode(any(String.class), any(BigDecimal.class));
    }

    @Test
    public void updateProductOk(){
        productService.updateProduct(product1.getCode(), product1.getName(), product1.getDescription(), product1.getPrice());
        verify(productRepository, times(1)).setProductNameByCode(any(String.class), any(String.class));
        verify(productRepository, times(1)).setProductDescriptionByCode(any(String.class), any(String.class));
        verify(productRepository, times(1)).setProductPriceByCode(any(String.class), any(BigDecimal.class));
    }


    @Test
    public void updateProductFailCodeNull(){
        productService.updateProduct(null, product1.getName(), product1.getDescription(), product1.getPrice());
        verify(productRepository, times(0)).setProductNameByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductDescriptionByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductPriceByCode(any(String.class), any(BigDecimal.class));
    }

    @Test
    public void updateProductFailCodeEmpty(){
        productService.updateProduct("", product1.getName(), product1.getDescription(), product1.getPrice());
        verify(productRepository, times(0)).setProductNameByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductDescriptionByCode(any(String.class), any(String.class));
        verify(productRepository, times(0)).setProductPriceByCode(any(String.class), any(BigDecimal.class));
    }
}
