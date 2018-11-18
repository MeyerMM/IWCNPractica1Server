package tarea.server.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tarea.server.model.Product;
import tarea.server.model.User;
import tarea.server.repositories.ProductRepository;
import tarea.server.repositories.UserRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseLoaderTests {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private DatabaseLoader databaseLoader;

    @Before
    public void setUp(){
        databaseLoader = new DatabaseLoader(productRepository, userRepository);
    }

    @Test
    public void checkLoader(){
        when(productRepository.save(any(Product.class))).thenReturn(new Product());
        when(userRepository.save(any(User.class))).thenReturn(new User());
        databaseLoader.initDatabase();
        verify(productRepository, times(2)).save(any(Product.class));
        verify(userRepository, times(2)).save(any(User.class));

    }

}