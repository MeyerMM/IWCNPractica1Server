package tarea.server.model;


import java.util.List;
import java.util.Optional;

public interface MovieList {
    List<Movie> getMovieList();  // Returns the movie list
    public List<Movie> findMovies(Optional<String> name,
                                  Optional<String> genre,
                                  Optional<String> director,
                                  Optional<String> cast,
                                  Optional<Integer> year,
                                  Optional<Float> score); // Find movies that match the given criteria
    boolean addMovie(Movie movie);  // Adds the movie received as an object. Returns true if successful, false if not
    int updateMovie(MovieDTO modifiedMovie); // Update one of the attributes of a movie. Returns the number of successful changes.
    Movie getMovie(Long code); // Returns a Movie object from its code
    boolean removeMovie(Long code); // Returns true if successful, false if not.

}
