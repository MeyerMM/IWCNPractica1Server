package Tarea3Server.Controller;

import Tarea3Server.Model.Product;
import Tarea3Server.Model.ProductList;

import java.util.List;
import java.util.Objects;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin
@Api(tags="Product")
public class ProductController {

    @Autowired
    private ProductList productList;

    // Show product list
    @GetMapping("/product/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value= "Getting all products")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = Product[].class),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Unhandled exception")
    })
    public List<Product> productList() {
        List<Product> products = productList.getProductList();
        return products;
    }

    // Show specific product, using its unique code
    @GetMapping("/product/find/{code}")
    @ApiOperation(value= "Get a product from its code")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Product Found", response = Product.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Product not found")
    })
    public ResponseEntity<Product> product(@PathVariable String code) {
        Product product = productList.getProduct(code);
        if(Objects.isNull(product)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    // Add a new product. If the product code is already in use, the old object will be replaced with the new one.
    @PostMapping("/product/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value= "Add a new product")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Created"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Bad Request"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Unhandled exception")
    })
    public void productSubmit(@RequestBody Product product) {
        productList.addProduct(product);
    }

    // Remove a product, using its unique code
    @DeleteMapping("/product/remove/{code}")
    @ApiOperation(value= "Remove a product")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Product removed", response = Product.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Product not found")
    })
    public ResponseEntity removeProduct(@PathVariable String code) {
        boolean success = productList.removeProduct(code);
        if(success){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
