package tarea.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tarea.server.repositories.UserRepository;
import tarea.server.model.User;

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
@Api(tags="User")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private Logger logger = LoggerFactory.getLogger(UserController.class);


    // Check if a username coincides with one in the database. Return its roles if it does.
    @GetMapping("/userAuthorization/roles/")
    @ApiOperation(value= "Return the roles of a user")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "User found", response = List.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "User not found")
    })
    public ResponseEntity<List<String>> getUserRoles(@ApiParam("Username of the user") @RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if(Objects.isNull(user)){
            logger.warn(String.format("User %s not found", username));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            logger.info(String.format("User %s found. Returning roles", username));
            return new ResponseEntity<>(user.getRoles(), HttpStatus.OK);
        }
    }

    // Check if a username coincides with one in the database. Return its encrypted password if it does.
    @GetMapping("/userAuthorization/password/")
    @ApiOperation(value= "Return the encrypted password of a user")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "User found", response = List.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "User not found")
    })
    public ResponseEntity<String> getUserPassword(@ApiParam("Username of the user") @RequestParam String username) {
        User user = userRepository.findByUsername(username);
        if(Objects.isNull(user)){
            logger.warn(String.format("User %s not found", username));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            logger.info(String.format("User %s found. Returning encrypted credentials", username));
            return new ResponseEntity<>(user.getPassword(), HttpStatus.OK);
        }
    }
}
