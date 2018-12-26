package tarea.server.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
    @InjectMocks
    private MovieService movieService;
    private Movie movie1;
    private Movie movie2;


    @Before
    public void setUp(){
        movieService = new MovieService(movieRepository);
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
        movie2.setScore((float)4.0);
        movie2.setTrailer("https://youtu.be/ZZyBaFYFySk");
        movie2.setPoster("https://m.media-amazon.com/images/M/MV5BZjZlZDlkYTktMmU1My00ZDBiLWFlNjEtYTBhNjVhOTM4ZjJjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");
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
        when( movieRepository.save(movie1)).thenReturn(movie2);
        boolean result = movieService.addMovie(movie1);
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).save(any(Movie.class));
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
        when(movieRepository.findByCode("CODE001")).thenReturn(expected);
        Movie result = movieService.getMovie("CODE001");
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findByCode(any(String.class));
    }

    @Test
    public void getMovieFail(){
        Movie expected = null;
        when(movieRepository.findByCode("CODE005")).thenReturn(expected);
        Movie result = movieService.getMovie("CODE005");
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findByCode(any(String.class));
    }



    @Test
    public void removeMovieOk(){
        Boolean expected = true;
        when(movieRepository.findByCode("CODE001")).thenReturn(movie1);
        Boolean result = movieService.removeMovie("CODE001");
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findByCode(any(String.class));
        verify(movieRepository, times(1)).removeByCode(any(String.class));
    }

    @Test
    public void removeMovieFail(){
        Boolean expected = false;
        when(movieRepository.findByCode("CODE005")).thenReturn(null);
        Boolean result = movieService.removeMovie("CODE005");
        Assert.assertEquals(result, expected);
        verify(movieRepository, times(1)).findByCode(any(String.class));
        verify(movieRepository, times(0)).removeByCode(any(String.class));
    }

    @Test
    public void updateWholeMovieOk(){
        int expected = 9;
        when(movieRepository.findByCode(movie2.getCode())).thenReturn(movie2);
        int result = movieService.updateMovie(movie2);
        verify(movieRepository, times(1)).setMovieNameByCode(any(String.class), any(String.class));
        verify(movieRepository, times(1)).setMovieDescriptionByCode(any(String.class), any(String.class));
        verify(movieRepository, times(1)).setMovieYearByCode(any(String.class), any(int.class));
        verify(movieRepository, times(1)).setMovieGenreByCode(any(String.class), any(String.class));
        verify(movieRepository, times(1)).setMovieDirectorByCode(any(String.class), any(String.class));
        verify(movieRepository, times(1)).setMovieCastByCode(any(String.class), any(String.class));
        verify(movieRepository, times(1)).setMovieScoreByCode(any(String.class), any(float.class));
        verify(movieRepository, times(1)).setMovieTrailerByCode(any(String.class), any(String.class));
        verify(movieRepository, times(1)).setMoviePosterByCode(any(String.class), any(String.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void updatePartialMovieOk(){
        int expected = 1;
        when(movieRepository.findByCode(movie1.getCode())).thenReturn(movie1);
        int result = movieService.updateMovie(movie1);
        verify(movieRepository, times(1)).setMovieNameByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearByCode(any(String.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreByCode(any(String.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterByCode(any(String.class), any(String.class));
        Assert.assertEquals(result, expected);
    }


    @Test
    public void updateMovieFailCodeNull(){
        int expected = 0;
        movie2.setCode(null);
        int result = movieService.updateMovie(movie2);
        verify(movieRepository, times(0)).setMovieNameByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearByCode(any(String.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreByCode(any(String.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterByCode(any(String.class), any(String.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void updateMovieFailCodeEmpty(){
        int expected = 0;
        movie2.setCode("");
        int result = movieService.updateMovie(movie2);
        verify(movieRepository, times(0)).setMovieNameByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearByCode(any(String.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreByCode(any(String.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterByCode(any(String.class), any(String.class));
        Assert.assertEquals(result, expected);
    }

    @Test
    public void updateMovieFailNameEmpty(){
        int expected = 0;
        movie2.setName("");
        int result = movieService.updateMovie(movie2);
        verify(movieRepository, times(0)).setMovieNameByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDescriptionByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieYearByCode(any(String.class), any(int.class));
        verify(movieRepository, times(0)).setMovieGenreByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieDirectorByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieCastByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMovieScoreByCode(any(String.class), any(float.class));
        verify(movieRepository, times(0)).setMovieTrailerByCode(any(String.class), any(String.class));
        verify(movieRepository, times(0)).setMoviePosterByCode(any(String.class), any(String.class));
        Assert.assertEquals(result, expected);
    }
}
