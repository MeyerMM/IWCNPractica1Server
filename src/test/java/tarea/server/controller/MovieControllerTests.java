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
import tarea.server.model.MovieDTO;
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
    private MovieDTO receivedMovie2;

    @Before
    public void setUp(){
        movieController = new MovieController(movieList);
        movie1 = new Movie(
                "Anihilation"
        );
        movie2 = new Movie(
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
        receivedMovie2 = new MovieDTO(
                "Donnie Darko"
        );
        receivedMovie2.setDescription("A troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes, after he narrowly escapes a bizarre accident.");
        receivedMovie2.setYear(2001);
        receivedMovie2.setGenre("Drama");
        receivedMovie2.setDirector("Richard Kelly");
        receivedMovie2.setCast("Jake Gyllenhaal, Jena Malone, Mary McDonnell");
        receivedMovie2.setScore((float)4);
        receivedMovie2.setTrailer("https://youtu.be/ZZyBaFYFySk");
        receivedMovie2.setPoster("https://m.media-amazon.com/images/M/MV5BZjZlZDlkYTktMmU1My00ZDBiLWFlNjEtYTBhNjVhOTM4ZjJjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");
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
        when(movieList.getMovie(movie1.getId())).thenReturn(movie1);
        ResponseEntity<Movie> result = movieController.getMovie(movie1.getId());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(Long.class));
    }

    @Test
    public void getMovieFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(movieList.getMovie(any(Long.class))).thenReturn(null);
        ResponseEntity<Movie> result = movieController.getMovie(movie1.getId());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(Long.class));
    }

    @Test
    public void modifyMovieOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.OK);
        receivedMovie2.setId(movie2.getId());  // The user will provide the Id of the movie to find
        when(movieList.getMovie(movie2.getId())).thenReturn(movie2);
        ResponseEntity<Movie> result = movieController.modifyMovie(receivedMovie2);
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(Long.class));
        verify(movieList, times(1)).updateMovie(any(MovieDTO.class));
    }

    @Test
    public void modifyMovieFailMovieNotFound() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        receivedMovie2.setId(movie2.getId());  // The user will provide the Id of the movie to find
        when(movieList.getMovie(any(Long.class))).thenReturn(null);
        ResponseEntity<Movie> result = movieController.modifyMovie(receivedMovie2);
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).getMovie(any(Long.class));
        verify(movieList, times(0)).updateMovie(any(MovieDTO.class));
    }

    @Test
    public void submitMovieOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.CREATED);
        when(movieList.addMovie(any(Movie.class))).thenReturn(true);
        ResponseEntity result =movieController.submitMovie(receivedMovie2);
        verify(movieList, times(1)).addMovie(any(Movie.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void submitMovieServerFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        when(movieList.addMovie(any(Movie.class))).thenReturn(false);
        ResponseEntity result =movieController.submitMovie(receivedMovie2);
        verify(movieList, times(1)).addMovie(any(Movie.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void submitMovieInputFail() {
        receivedMovie2.setName("");
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        ResponseEntity result =movieController.submitMovie(receivedMovie2);
        verify(movieList, times(0)).addMovie(any(Movie.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void removeMovieOk() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.OK);
        when(movieList.removeMovie(movie1.getId())).thenReturn(true);
        ResponseEntity<Movie> result = movieController.removeMovie(movie1.getId());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).removeMovie(movie1.getId());
        verify(movieList, times(1)).removeMovie(any(Long.class));
    }

    @Test
    public void removeMovieFail() {
        ResponseEntity expected = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        when(movieList.removeMovie(movie1.getId())).thenReturn(false);
        ResponseEntity<Movie> result = movieController.removeMovie(movie1.getId());
        Assert.assertEquals(result, expected);
        verify(movieList, times(1)).removeMovie(movie1.getId());
        verify(movieList, times(1)).removeMovie(any(Long.class));
    }
}
