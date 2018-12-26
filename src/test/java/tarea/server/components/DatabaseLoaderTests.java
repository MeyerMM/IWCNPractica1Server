package tarea.server.components;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tarea.server.model.Movie;
import tarea.server.repositories.MovieRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseLoaderTests {
    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private DatabaseLoader databaseLoader;

    @Before
    public void setUp(){
        databaseLoader = new DatabaseLoader(movieRepository);
    }

    @Test
    public void checkLoader(){
        when(movieRepository.save(any(Movie.class))).thenReturn(new Movie());
        databaseLoader.initDatabase();
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

}