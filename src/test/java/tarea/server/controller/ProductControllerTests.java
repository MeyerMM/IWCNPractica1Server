package tarea.server.controller;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tarea.server.model.Product;
import tarea.server.model.ProductList;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ProductControllerTests {
    @Mock
    private ProductList productList;
    @InjectMocks
    private ProductController productController;
    private Product product1;
    private Product product2;

    @Before
    public void setUp(){
        productController = new ProductController(productList);
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
        when(productList.getProductList()).thenReturn(expected);
        List<Product> result = productController.getProductList();
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).getProductList();
    }

    @Test
    public void getProductOk() {
        ResponseEntity<Product> expected = new ResponseEntity<>(product1, HttpStatus.OK);
        when(productList.getProduct(product1.getCode())).thenReturn(product1);
        ResponseEntity<Product> result = productController.getProduct(product1.getCode());
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).getProduct(any(String.class));
    }

    @Test
    public void getProductFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(productList.getProduct(any(String.class))).thenReturn(null);
        ResponseEntity<Product> result = productController.getProduct(product1.getCode());
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).getProduct(any(String.class));
    }

    @Test
    public void modifyProductOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.OK);
        when(productList.getProduct(product1.getCode())).thenReturn(product1);
        ResponseEntity<Product> result = productController.modifyProduct(product1);
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).getProduct(any(String.class));
        verify(productList, times(1)).updateProduct(any(String.class),any(String.class),any(String.class),any(BigDecimal.class));
    }

    @Test
    public void modifyProductFailProductNotFound() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(productList.getProduct(product1.getCode())).thenReturn(null);
        ResponseEntity<Product> result = productController.modifyProduct(product1);
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).getProduct(any(String.class));
        verify(productList, times(0)).updateProduct(any(String.class),any(String.class),any(String.class),any(BigDecimal.class));
    }

    @Test
    public void modifyProductFailBadFormat() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ResponseEntity<Product> result = productController.modifyProduct(null);
        Assert.assertEquals(result, expected);
        verify(productList, times(0)).getProduct(any(String.class));
        verify(productList, times(0)).updateProduct(any(String.class),any(String.class),any(String.class),any(BigDecimal.class));
    }

    @Test
    public void submitProduct() {
        productController.submitProduct(product1);
        verify(productList, times(1)).addProduct(product1);
    }

    @Test
    public void removeProductOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.OK);
        when(productList.removeProduct(product1.getCode())).thenReturn(true);
        ResponseEntity<Product> result = productController.removeProduct(product1.getCode());
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).removeProduct(product1.getCode());
        verify(productList, times(1)).removeProduct(any(String.class));
    }

    @Test
    public void removeProductFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(productList.removeProduct(product1.getCode())).thenReturn(false);
        ResponseEntity<Product> result = productController.removeProduct(product1.getCode());
        Assert.assertEquals(result, expected);
        verify(productList, times(1)).removeProduct(product1.getCode());
        verify(productList, times(1)).removeProduct(any(String.class));
    }
}
