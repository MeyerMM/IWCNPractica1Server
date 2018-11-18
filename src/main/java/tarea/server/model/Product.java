package tarea.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;

@ApiModel(description = "Class that represents a product")
@Entity
public class Product {
    @NotBlank
    @ApiModelProperty(notes = "Unique code that identifies the product", example = "\"3\"", required = true)
    @Id
    private String code;
    @ApiModelProperty(notes = "Name of the product", example = "Minalba")
    private String name;
    @ApiModelProperty(notes = "Description of the product", example = "Bottled water")
    private String description;
    @Min(0)
    @ApiModelProperty(notes = "Price of the product", example = "1.50")
    private BigDecimal price;

    public Product() {
    }

    public Product(String code, String name, String description, BigDecimal price) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
