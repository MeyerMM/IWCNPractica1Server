package tarea.server.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import tarea.server.externalapi.ExternalAPIHandler;
import tarea.server.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class MovieServiceTests {
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ExternalAPIHandler externalHandler;
    @InjectMocks
    private MovieService movieService;
    private Movie movie1;
    private Movie movie2;
    private Movie incompleteMovie2;
    private MovieDTO receivedMovie1;
    private MovieDTO receivedMovie2;


    @Before
    public void setUp(){
       movieService = new MovieService(movieRepository, externalHandler);
       movie1 = new Movie(
                "Anihilation"
        );
        movie2 = new Movie(
                "Donnie Darko"
        );
        incompleteMovie2 = new Movie(
                "Donnie Darko"
        );
        movie2.setDescription("A troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes, after he narrowly escapes a bizarre accident.");
        movie2.setYear(2001);
        movie2.setGenre("Drama");
        movie2.setDirector("Richard Kelly");
        movie2.setCast("Jake Gyllenhaal, Jena Malone, Mary McDonnell");
        movie2.setScore((float)4.0);
        movie2.setTrailer("https://youtu.be/ZZyBaFYFySk");
        movie2.setPoster("https://m.media-amazon.com/images/M/MV5BZjZlZDlkYTktMmU1My00ZDBiLWFlNjEtYTBhNjVhOTM4ZjJjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");
        receivedMovie1 = new MovieDTO(
                "Anihilation"
        );
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
        List<Movie> expected = new ArrayList<>();
        expected.add(movie1);
        expected.add(movie2);
        when(movieRepository.findAll()).thenReturn(expected);
        List<Movie> result = movieService.getMovieList();
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    public void findMoviesOk(){
        List<Movie> expected = new ArrayList<>();
        expected.add(movie2);
        when(movieRepository.findAll(any(Specification.class))).thenReturn(expected);
        Optional<String> movieName = Optional.of(movie2.getName());
        Optional<String> movieGenre = Optional.of(movie2.getGenre());
        Optional<String> movieDirector = Optional.of(movie2.getDirector());
        Optional<String> movieCast = Optional.of(movie2.getCast());
        Optional<Integer> movieYear = Optional.of(movie2.getYear());
        Optional<Float> movieScore = Optional.of(movie2.getScore());
        List<Movie> result = movieService.findMovies(movieName, movieGenre, movieDirector, movieCast, movieYear, movieScore);
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    public void findMoviesFail(){
        List<Movie> expected = new ArrayList<>();
        Optional<String> emptyString = Optional.empty();
        Optional<Integer> emptyInteger = Optional.empty();
        Optional<Float> emptyFloat = Optional.empty();
        List<Movie> result = movieService.findMovies(emptyString, emptyString, emptyString, emptyString, emptyInteger, emptyFloat);
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(0)).findAll(any(Specification.class));
    }

    @Test
    public void addMovieOk(){
        boolean expected = true;
        when( externalHandler.completeMovie(incompleteMovie2)).thenReturn(movie2);
        when( movieRepository.save(movie2)).thenReturn(movie2);
        boolean result = movieService.addMovie(incompleteMovie2);
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).save(any(Movie.class));
        verify(externalHandler, times(1)).completeMovie(any(Movie.class));
    }

    @Test
    public void addMovieFail(){
        boolean expected = false;
        when( movieRepository.save(movie1)).thenReturn(null);
        boolean result = movieService.addMovie(movie1);
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).save(any(Movie.class));
    }

    @Test
    public void getMovieOk(){
        Movie expected = movie1;
        when(movieRepository.findById(movie1.getId())).thenReturn(Optional.of(expected));
        Movie result = movieService.getMovie(movie1.getId());
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void getMovieFail(){
        Movie expected = null;
        when(movieRepository.findById(movie2.getId()+1)).thenReturn(Optional.empty());
        Movie result = movieService.getMovie(movie2.getId()+1);
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findById(any(Long.class));
    }



    @Test
    public void removeMovieOk(){
        Boolean expected = true;
        when(movieRepository.findById(movie1.getId())).thenReturn(Optional.of(movie1));
        Boolean result = movieService.removeMovie(movie1.getId());
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findById(any(Long.class));
        verify(movieRepository, times(1)).removeById(any(Long.class));
    }

    @Test
    public void removeMovieFail(){
        Boolean expected = false;
        when(movieRepository.findById(any(Long.class))).thenReturn(Optional.empty());
        Boolean result = movieService.removeMovie(movie2.getId());
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findById(any(Long.class));
        verify(movieRepository, times(0)).removeById(any(Long.class));
    }

    @Test
    public void updateWholeMovieOk(){
        int expected = 9;
        receivedMovie2.setId(movie2.getId());  // The user will provide the Id of the movie to find
        when(movieRepository.findById(receivedMovie2.getId())).thenReturn(Optional.of(movie2));
        int result = movieService.updateMovie(receivedMovie2);
        verify(movieRepository, times(1)).setMovieNameById(any(Long.class), any(String.class));
        verify(movieRepository, times(1)).setMovieDescriptionById(any(Long.class), any(String.class));
        verify(movieRepository, times(1)).setMovieYearById(any(Long.class), any(int.class));
        verify(movieRepository, times(1)).setMovieGenreById(any(Long.class), any(String.class));
        verify(movieRepository, times(1)).setMovieDirectorById(any(Long.class), any(String.class));
        verify(movieRepository, times(1)).setMovieCastById(any(Long.class), any(String.class));
        verify(movieRepository, times(1)).setMovieScoreById(any(Long.class), any(float.class));
        verify(movieRepository, times(1)).setMovieTrailerById(any(Long.class), any(String.class));
        verify(movieRepository, times(1)).setMoviePosterById(any(Long.class), any(String.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void updatePartialMovieOk(){
        int expected = 1;
        receivedMovie1.setId(movie1.getId());  // The user will provide the Id of the movie to find
        when(movieRepository.findById(receivedMovie1.getId())).thenReturn(Optional.of(movie1));
        int result = movieService.updateMovie(receivedMovie1);
        verify(movieRepository, times(1)).setMovieNameById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearById(any(Long.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreById(any(Long.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterById(any(Long.class), any(String.class));
        Assert.assertEquals(result, expected);
    }


    @Test
    public void updateMovieFailCodeNull(){
        int expected = 0;
        receivedMovie2.setId(null);
        int result = movieService.updateMovie(receivedMovie2);
        verify(movieRepository, times(0)).setMovieNameById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearById(any(Long.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreById(any(Long.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterById(any(Long.class), any(String.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void updateMovieFailNameEmpty(){
        int expected = 0;
        receivedMovie2.setName("");
        int result = movieService.updateMovie(receivedMovie2);
        verify(movieRepository, times(0)).setMovieNameById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearById(any(Long.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreById(any(Long.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerById(any(Long.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterById(any(Long.class), any(String.class));
        Assert.assertEquals(result, expected);
    }
}
