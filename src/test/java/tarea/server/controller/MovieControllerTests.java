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
import tarea.server.model.Movie;
import tarea.server.model.MovieList;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class MovieControllerTests {
    @Mock
    private MovieList movieList;
    @InjectMocks
    private MovieController movieController;
    private Movie movie1;
    private Movie movie2;

    @Before
    public void setUp(){
        movieController = new MovieController(movieList);
        movie1 = new Movie(
                "CODE001",
                "Anihilation"
        );
        movie2 = new Movie(
                "CODE002",
                "Donnie Darko"
        );
        movie2.setDescription("A troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes, after he narrowly escapes a bizarre accident.");
        movie2.setYear(2001);
        movie2.setGenre("Drama");
        movie2.setDirector("Richard Kelly");
        movie2.setCast("Jake Gyllenhaal, Jena Malone, Mary McDonnell");
        movie2.setScore((float)4);
        movie2.setTrailer("https://youtu.be/ZZyBaFYFySk");
        movie2.setPoster("https://m.media-amazon.com/images/M/MV5BZjZlZDlkYTktMmU1My00ZDBiLWFlNjEtYTBhNjVhOTM4ZjJjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");
    }

    @Test
    public void getMovieList(){
        List<Movie> expected = new LinkedList<>();
        expected.add(movie1);
        expected.add(movie2);
        when(movieList.getMovieList()).thenReturn(expected);
        List<Movie> result = movieController.getMovieList();
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovieList();
    }

    @Test
    public void findMoviesOk() {
        List<Movie> expectedList = new LinkedList<>();
        expectedList.add(movie2);
        HttpStatus expectedStatus = HttpStatus.OK;
        Optional<String> movieName = Optional.of(movie2.getName());
        Optional<String> emptyString = Optional.empty();
        Optional<Integer> emptyInteger = Optional.empty();
        Optional<Float> emptyFloat = Optional.empty();
        when(movieList.findMovies(movieName, emptyString, emptyString, emptyString, emptyInteger, emptyFloat)).thenReturn(expectedList);
        ResponseEntity<List<Movie>> result = movieController.findMovies(movieName, emptyString, emptyString, emptyString, emptyInteger, emptyFloat);
        Assert.assertEquals(result.getBody(), expectedList);
        Assert.assertEquals(result.getStatusCode(), expectedStatus);
        verify(movieList, times(1)).findMovies(any(Optional.class),any(Optional.class),any(Optional.class),any(Optional.class), any(Optional.class),any(Optional.class));
    }

    @Test
    public void findMoviesFail() {
        List<Movie> expectedList = new LinkedList<>();
        HttpStatus expectedStatus = HttpStatus.NOT_FOUND;
        Optional<String> movieName = Optional.of(movie2.getName()+"ZZZ");
        Optional<String> emptyString = Optional.empty();
        Optional<Integer> emptyInteger = Optional.empty();
        Optional<Float> emptyFloat = Optional.empty();
        when(movieList.findMovies(movieName, emptyString, emptyString, emptyString, emptyInteger, emptyFloat)).thenReturn(expectedList);
        ResponseEntity<List<Movie>> result = movieController.findMovies(movieName, emptyString, emptyString, emptyString, emptyInteger, emptyFloat);
        Assert.assertEquals(result.getStatusCode(), expectedStatus);
        verify(movieList, times(1)).findMovies(any(Optional.class),any(Optional.class),any(Optional.class),any(Optional.class), any(Optional.class),any(Optional.class));
    }

    @Test
    public void getMovieOk() {
        ResponseEntity<Movie> expected = new ResponseEntity<>(movie1, HttpStatus.OK);
        when(movieList.getMovie(movie1.getCode())).thenReturn(movie1);
        ResponseEntity<Movie> result = movieController.getMovie(movie1.getCode());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(String.class));
    }

    @Test
    public void getMovieFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(movieList.getMovie(any(String.class))).thenReturn(null);
        ResponseEntity<Movie> result = movieController.getMovie(movie1.getCode());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(String.class));
    }

    @Test
    public void modifyMovieOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.OK);
        when(movieList.getMovie(movie2.getCode())).thenReturn(movie2);
        ResponseEntity<Movie> result = movieController.modifyMovie(movie2);
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(String.class));
        verify(movieList, times(1)).updateMovie(any(Movie.class));
    }

    @Test
    public void modifyMovieFailMovieNotFound() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(movieList.getMovie(movie2.getCode())).thenReturn(null);
        ResponseEntity<Movie> result = movieController.modifyMovie(movie2);
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(String.class));
        verify(movieList, times(0)).updateMovie(any(Movie.class));
    }

    @Test
    public void submitMovie() {
        movieController.submitMovie(movie1);
        verify(movieList, times(1)).addMovie(movie1);
    }

    @Test
    public void removeMovieOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.OK);
        when(movieList.removeMovie(movie1.getCode())).thenReturn(true);
        ResponseEntity<Movie> result = movieController.removeMovie(movie1.getCode());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).removeMovie(movie1.getCode());
        verify(movieList, times(1)).removeMovie(any(String.class));
    }

    @Test
    public void removeMovieFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(movieList.removeMovie(movie1.getCode())).thenReturn(false);
        ResponseEntity<Movie> result = movieController.removeMovie(movie1.getCode());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).removeMovie(movie1.getCode());
        verify(movieList, times(1)).removeMovie(any(String.class));
    }
}
