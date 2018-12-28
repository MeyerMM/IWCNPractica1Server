package tarea.server.externalapi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import tarea.server.model.Movie;
import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class ExternalAPIHandlerTests {
    @Mock
    private RestTemplate restTemplate;
    @InjectMocks
    private ExternalAPIHandler externalHandler;
    private OMDbMovie externalMovie;
    private Movie internalMovie;
    private Movie completeMovie;

    @Before
    public void setUp(){
        externalHandler = new ExternalAPIHandler(restTemplate);
        internalMovie = new Movie(
                "Donnie Darko"
        );
        externalMovie = new OMDbMovie();
        externalMovie.setTitle("Donnie Darko");
        externalMovie.setPlot("A troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes, after he narrowly escapes a bizarre accident.");
        externalMovie.setYear("2001");
        externalMovie.setGenre("Drama");
        externalMovie.setDirector("Richard Kelly");
        externalMovie.setActors("Jake Gyllenhaal, Jena Malone, Mary McDonnell");
        externalMovie.setMetascore("90");
        externalMovie.setPoster("https://m.media-amazon.com/images/M/MV5BZjZlZDlkYTktMmU1My00ZDBiLWFlNjEtYTBhNjVhOTM4ZjJjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");
        externalMovie.setResponse("True");
        externalMovie.setImdbID("1111");
        completeMovie = new Movie(
                "Donnie Darko"
        );
        completeMovie.setDescription("A troubled teenager is plagued by visions of a man in a large rabbit suit who manipulates him to commit a series of crimes, after he narrowly escapes a bizarre accident.");
        completeMovie.setYear(2001);
        completeMovie.setGenre("Drama");
        completeMovie.setDirector("Richard Kelly");
        completeMovie.setCast("Jake Gyllenhaal, Jena Malone, Mary McDonnell");
        completeMovie.setScore((float)4.5);
        completeMovie.setTrailer("https://youtu.be/ZZyBaFYFySk");
        completeMovie.setPoster("https://m.media-amazon.com/images/M/MV5BZjZlZDlkYTktMmU1My00ZDBiLWFlNjEtYTBhNjVhOTM4ZjJjXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_.jpg");

    }

    @Test
    public void getMovieFromOMDbOk(){
        externalMovie.setResponse("True");
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(externalMovie);
        OMDbMovie result = externalHandler.getMovieFromOMDb(internalMovie);
        Assert.assertEquals(result, externalMovie);
        verify(restTemplate, times(1)).getForObject(any(String.class),  any(Class.class));
    }

    @Test
    public void getMovieFromOMDbExternalError(){
        externalMovie.setResponse("False");
        OMDbMovie expected = null;
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(externalMovie);
        OMDbMovie result = externalHandler.getMovieFromOMDb(internalMovie);
        Assert.assertEquals(result, expected);
        verify(restTemplate, times(1)).getForObject(any(String.class),  any(Class.class));
    }

    @Test
    public void getMovieFromOMDbInternalError(){
        OMDbMovie expected = null;
        internalMovie.setName("");
        OMDbMovie result = externalHandler.getMovieFromOMDb(internalMovie);
        Assert.assertEquals(result, expected);
        verify(restTemplate, times(0)).getForObject(any(String.class),  any(Class.class));
    }

    @Test
    public void completeMovieTextOk(){
        Movie expected = completeMovie;
        Movie result = externalHandler.completeMovieText(internalMovie, externalMovie);
        Assert.assertEquals(result.getName(), expected.getName());
        Assert.assertEquals(result.getDescription(), expected.getDescription());
        Assert.assertEquals(result.getYear(), expected.getYear());
        Assert.assertEquals(result.getGenre(), expected.getGenre());
        Assert.assertEquals(result.getDirector(), expected.getDirector());
        Assert.assertEquals(result.getCast(), expected.getCast());
        Assert.assertEquals(result.getScore(), expected.getScore());
        Assert.assertEquals(result.getPoster(), expected.getPoster());
    }

    @Test
    public void completeMovieTextFail(){
        Movie expected = null;
        Movie result = externalHandler.completeMovieText(internalMovie, null);
        Assert.assertEquals(result, expected);
    }

    @Test
    public void completeMovieTextUpdateNotNeeded(){
        Movie expected = completeMovie;
        Movie result = externalHandler.completeMovieText(completeMovie, externalMovie);
        Assert.assertEquals(result, expected);
    }

    @Test
    public void getMovieFromTheMovieDBFailNullId(){
        Movie expected = null;
        TheMovieDBMovieResults result = externalHandler.getMovieFromTheMovieDB(null);
        Assert.assertEquals(result, expected);
    }

    @Test
    public void getMovieFromTheMovieDBFailNoMovies(){
        Movie expected = null;
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(new TheMovieDBFindResponse());
        TheMovieDBMovieResults result = externalHandler.getMovieFromTheMovieDB("111");
        Assert.assertEquals(result, expected);
    }

    @Test
    public void getMovieFromTheMovieDBOk(){
        // Configuration of inputs and outputs
        TheMovieDBFindResponse requestResponse = new TheMovieDBFindResponse();
        TheMovieDBMovieResults[] requestResults = new TheMovieDBMovieResults[1];
        requestResponse.setMovieResults(requestResults);
        requestResults[0] = new TheMovieDBMovieResults();
        requestResults[0].setId("5");
        requestResults[0].setPosterPath("123.jpg");
        // Test
        TheMovieDBMovieResults expected = requestResults[0];
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(requestResponse);
        TheMovieDBMovieResults result = externalHandler.getMovieFromTheMovieDB("111");
        Assert.assertEquals(result, expected);
    }

    @Test
    public void getVideoFromTheMovieDBFail(){
        TheMovieDBVideoResult expected = null;
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(new TheMovieDBVideosResponse());
        TheMovieDBVideoResult result = externalHandler.getVideoFromTheMovieDB("111");
        Assert.assertEquals(result, expected);
    }

    @Test
    public void getVideoFromTheMovieDBOk(){
        // Configuration of inputs and outputs
        TheMovieDBVideosResponse requestResponse = new TheMovieDBVideosResponse();
        TheMovieDBVideoResult[] requestResults = new TheMovieDBVideoResult[1];
        requestResponse.setResults(requestResults);
        requestResults[0] = new TheMovieDBVideoResult();
        requestResults[0].setKey("123");
        requestResults[0].setSite("youtube");
        // Test
        TheMovieDBVideoResult expected = requestResults[0];
        when(restTemplate.getForObject(any(String.class), any(Class.class))).thenReturn(requestResponse);
        TheMovieDBVideoResult result = externalHandler.getVideoFromTheMovieDB("111");
        Assert.assertEquals(result, expected);
    }

    @Test
    public void completeMovieFail(){
        Movie expected = null;
        internalMovie.setName(null);
        Movie result = externalHandler.completeMovie(internalMovie);
        Assert.assertEquals(result, expected);
    }

    // Test completeMovieOk would be redundant with the previous tests.
    // Also, it would need a way to distinguish between the different restTemplate.getForObject.
    // No effective or practical way of doing this is known at the moment.

}
