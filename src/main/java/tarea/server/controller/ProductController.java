package tarea.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tarea.server.model.Product;
import tarea.server.model.ProductList;

import java.util.List;
import java.util.Objects;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin
@Api(tags="Product")
public class ProductController {
    private ProductList productList;
    private Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    public ProductController(ProductList productList) {
        this.productList = productList;
    }

    // Show product list
    @GetMapping("/product/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value= "Getting all products")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = Product[].class),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Unhandled exception")
    })
    public List<Product> getProductList() {
        logger.info("Product list shown");
        return productList.getProductList();
    }

    // Show specific product, using its unique code
    @GetMapping("/product/find/{code}")
    @ApiOperation(value= "Get a product from its code")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Product Found", response = Product.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Product not found")
    })
    public ResponseEntity<Product> getProduct(@ApiParam("Unique code identifier of the product") @PathVariable String code) {
        Product product = productList.getProduct(code);
        if(Objects.isNull(product)){
            logger.warn("Product with code " + code + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            logger.info("Product with code " + code + " found");
            return new ResponseEntity<>(product, HttpStatus.OK);
        }
    }

    // Modify a product
    @PutMapping ("/product/modify")
    @ApiOperation(value= "Modify a specific product")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Product Modified"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Product not found"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Product with wrong format")
    })
    public ResponseEntity modifyProduct(@ApiParam("Product object to be submitted") @RequestBody Product modifiedProduct) {
            Product originalProduct = productList.getProduct(modifiedProduct.getCode());
            if(Objects.isNull(originalProduct)){
                logger.warn("Product with code " + modifiedProduct.getCode() + " not found. Modification failed");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else{
                productList.updateProduct(modifiedProduct.getCode(),
                        modifiedProduct.getName(),
                        modifiedProduct.getDescription(),
                        modifiedProduct.getPrice()
                );
                logger.info("Product with code " +  modifiedProduct.getCode() + " modified");
                return new ResponseEntity<>(HttpStatus.OK);
            }
    }

    // Add a new product.
    @PostMapping("/product/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value= "Add a new product")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Created"),
    })
    public void submitProduct(@ApiParam("Product object to be submitted") @RequestBody Product product) {
        logger.info("New product added");
        productList.addProduct(product);
    }

    // Remove a product, using its unique code
    @DeleteMapping("/product/remove/{code}")
    @ApiOperation(value= "Remove a product")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Product removed", response = Product.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Product not found")
    })
    public ResponseEntity removeProduct(@ApiParam("Unique code identifier of the product") @PathVariable String code) {
        boolean success = productList.removeProduct(code);
        if(success){
            logger.info("Product of code " + code +" removed");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            logger.warn("Product with code " + code + " not found. Elimination failed");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
