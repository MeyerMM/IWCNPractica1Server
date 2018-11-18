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
import tarea.server.model.User;
import tarea.server.repositories.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTests {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserController userController;
    private User user1;


    @Before
    public void setUp(){
        userController = new UserController(userRepository);
        List<String> userRoles = new LinkedList<>();
        userRoles.add("ROLE_VIEW");
        user1 = new User("User","$2a$04$DP1EadCzK7tDCspks.B4MO0iOE2hUoPY3AVelG0kjALRcoLU52qZG", userRoles);
    }

    @Test
    public void getUserRolesOk(){
        ResponseEntity<List<String>> expected = new ResponseEntity<>(user1.getRoles(),HttpStatus.OK);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        ResponseEntity<List<String>> result = userController.getUserRoles(user1.getUsername());
        Assert.assertEquals(result, expected);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).findByUsername(user1.getUsername());
    }

    @Test
    public void getUserRolesFail(){
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(null);
        ResponseEntity<List<String>> result = userController.getUserRoles(user1.getUsername());
        Assert.assertEquals(result, expected);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).findByUsername(user1.getUsername());
    }

    @Test
    public void getUserPasswordOk(){
        ResponseEntity<String> expected = new ResponseEntity<>(user1.getPassword(),HttpStatus.OK);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(user1);
        ResponseEntity<String> result = userController.getUserPassword(user1.getUsername());
        Assert.assertEquals(result, expected);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).findByUsername(user1.getUsername());
    }

    @Test
    public void getUserPasswordFail(){
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(userRepository.findByUsername(user1.getUsername())).thenReturn(null);
        ResponseEntity result = userController.getUserPassword(user1.getUsername());
        Assert.assertEquals(result, expected);
        verify(userRepository, times(1)).findByUsername(any(String.class));
        verify(userRepository, times(1)).findByUsername(user1.getUsername());
    }

}
