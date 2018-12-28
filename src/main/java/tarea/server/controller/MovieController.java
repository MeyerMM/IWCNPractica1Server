package tarea.server.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tarea.server.model.Movie;
import tarea.server.model.MovieList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin
@Api(tags="Movie")
public class MovieController {
    private MovieList movieList;
    private Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    public MovieController(MovieList movieList) {
        this.movieList = movieList;
    }

    // Show movie list
    @GetMapping("/movie/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value= "Getting all movies")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "OK", response = Movie[].class),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Unhandled exception")
    })
    public List<Movie> getMovieList() {
        logger.info("Movie list shown");
        return movieList.getMovieList();
    }

    // Show specific movie, using its unique code
    @GetMapping("/movie/find/{code}")
    @ApiOperation(value= "Get a movie from its code")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Movie Found", response = Movie.class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Movie not found")
    })
    public ResponseEntity<Movie> getMovie(@ApiParam("Unique code identifier of the movie") @PathVariable Long code) {
        Movie movie = movieList.getMovie(code);
        if (Objects.isNull(movie)) {
            logger.warn(String.format("Movie with code %s not found", code));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            logger.info(String.format("Movie with code %s found", code));
            return new ResponseEntity<>(movie, HttpStatus.OK);
        }
    }

    @GetMapping("/movie/find")
    @ApiOperation(value= "Get a  list of movies from one or many of its attributes")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Movie Found", response = Movie[].class),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Movie not found")
    })
    public ResponseEntity<List<Movie>> findMovies(
            @ApiParam("Optional Parameter: Name of the movie") @RequestParam Optional<String> name,
            @ApiParam("Optional Parameter: Genre of the movie") @RequestParam Optional<String> genre,
            @ApiParam("Optional Parameter: Director of the movie") @RequestParam Optional<String> director,
            @ApiParam("Optional Parameter: Cast of the movie") @RequestParam Optional<String> cast,
            @ApiParam("Optional Parameter: Year of release of the movie") @RequestParam Optional<Integer> year,
            @ApiParam("Optional Parameter: Non inclusive, minimum score of the movie") @RequestParam Optional<Float> score) {

        List<Movie> foundMovies = movieList.findMovies(name, genre, director, cast, year, score);
        if(foundMovies.isEmpty()){
           logger.warn("No movies found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(foundMovies, HttpStatus.OK);
        }
    }

    // Modify a movie
    @PutMapping ("/movie/modify")
    @ApiOperation(value= "Modify a specific movie.")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Movie Modified"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Movie not found"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Movie with wrong format")
    })
    public ResponseEntity modifyMovie(@ApiParam("Movie object to be modified") @RequestBody Movie modifiedMovie) {
            Movie originalMovie = movieList.getMovie(modifiedMovie.getId());
            if(Objects.isNull(originalMovie)){
                logger.warn(String.format("Movie with code %s not found. Modification failed", modifiedMovie.getId()));
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else{
                int numberOfChanges = movieList.updateMovie(modifiedMovie);
                logger.info(String.format("Movie with code %s modified %d times", modifiedMovie.getId(), numberOfChanges));
                return new ResponseEntity<>(HttpStatus.OK);
            }
    }

    // Add a new movie.
    @PostMapping("/movie/add")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value= "Add a new movie")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_CREATED, message = "Created"),
            @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = "Movie not created"),
            @ApiResponse(code = HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message = "Movie not created")
    })
    public ResponseEntity submitMovie(@ApiParam("Movie object to be submitted") @RequestBody Movie submittedMovie) {
        if(Objects.isNull(submittedMovie.getName()) || submittedMovie.getName().isEmpty()){
            logger.info("Movie addition failed due to invalid name");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else{
            boolean success = movieList.addMovie(submittedMovie);
            if(success){
                logger.info("New movie added");
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
            else{
                logger.info("Movie addition failed due to internal error");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
    }

    // Remove a movie, using its unique code
    @DeleteMapping("/movie/remove/{code}")
    @ApiOperation(value= "Remove a movie")
    @ApiResponses({
            @ApiResponse(code = HttpServletResponse.SC_OK, message = "Movie removed"),
            @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = "Movie not found")
    })
    public ResponseEntity removeMovie(@ApiParam("Unique code identifier of the movie") @PathVariable Long code) {
        boolean success = movieList.removeMovie(code);
        if(success){
            logger.info(String.format("Movie with code %s removed", code));
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            logger.warn(String.format("Movie with code %s not found. Elimination failed.", code));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
