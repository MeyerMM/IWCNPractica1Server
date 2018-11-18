package tarea.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import java.util.List;

@ApiModel(description = "Class that represents a user")
@Entity
public class User {
    @NotBlank
    @ApiModelProperty(notes = "Unique name that identifies the user", example = "\"bestUsernameEver\"", required = true)
    @Id
    private String username;
    @ApiModelProperty(notes = "Private password of the user", example = "superpassword")
    private String password;
    @ApiModelProperty(notes = "Collection of roles assigned to the user", example = "ROLE_USER")
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

    public User() {
    }

    public User(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

}