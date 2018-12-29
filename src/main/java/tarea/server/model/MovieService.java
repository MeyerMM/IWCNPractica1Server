package tarea.server.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tarea.server.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import tarea.server.externalapi.ExternalAPIHandler;

@Service
public class MovieService implements MovieList {
    private MovieRepository movieRepository;
    private ExternalAPIHandler externalHandler;

    @Autowired
    public MovieService(MovieRepository movieRepository, ExternalAPIHandler externalHandler){
        this.movieRepository = movieRepository;
        this.externalHandler = externalHandler;
    }
    public List<Movie> getMovieList(){
        // Returns the movie list
        return movieRepository.findAll();
    }

    public List<Movie> findMovies(Optional<String> name,
                                  Optional<String> genre,
                                  Optional<String> director,
                                  Optional<String> cast,
                                  Optional<Integer> year,
                                  Optional<Float> score){
        int parameterCount = 0;
        String operationContains = "Contains";
        String operationEquals = "Equals";
        String operationGreater = "Greater Than";
        // Initialize Specifications so they all include all the rows
        MovieSpecifications nameSpec = new MovieSpecifications(new SearchCriteria("id", operationGreater, "0"));
        MovieSpecifications genreSpec = new MovieSpecifications(new SearchCriteria("id", operationGreater, "0"));
        MovieSpecifications directorSpec = new MovieSpecifications(new SearchCriteria("id", operationGreater, "0"));
        MovieSpecifications castSpec = new MovieSpecifications(new SearchCriteria("id", operationGreater, "0"));
        MovieSpecifications yearSpec = new MovieSpecifications(new SearchCriteria("id", operationGreater, "0"));
        MovieSpecifications scoreSpec = new MovieSpecifications(new SearchCriteria("id", operationGreater, "0"));


        if(name.isPresent()){
            nameSpec = new MovieSpecifications(new SearchCriteria("name", operationContains, name.get()));
            parameterCount++;
        }
        if(genre.isPresent()){
            genreSpec = new MovieSpecifications(new SearchCriteria("genre", operationContains, genre.get()));
            parameterCount++;
        }
        if(director.isPresent()){
            directorSpec = new MovieSpecifications(new SearchCriteria("director", operationContains, director.get()));
            parameterCount++;
        }
        if(cast.isPresent()){
            castSpec = new MovieSpecifications(new SearchCriteria("cast", operationContains, cast.get()));
            parameterCount++;
        }
        if(year.isPresent()){
            yearSpec = new MovieSpecifications(new SearchCriteria("year", operationEquals, year.get().toString()));
            parameterCount++;
        }
        if(score.isPresent()){
            scoreSpec = new MovieSpecifications(new SearchCriteria("score", operationGreater, score.get().toString()));
            parameterCount++;
        }

        if(parameterCount > 0){
            return movieRepository.findAll(Specification.where(nameSpec).and(genreSpec).and(directorSpec).and(castSpec).and(yearSpec).and(scoreSpec));
        }
        else{
            return new ArrayList<>();
        }
    }

    public boolean addMovie(Movie movie){
        // Adds the movie received as an object. Completes its data with an external API.
        // If external data cannot be found, it stores the movie as it was received.
        // Returns true if successful, false if not
        Movie addedMovie;
        Movie completeMovie = externalHandler.completeMovie(movie);
        if(Objects.isNull(completeMovie)){
            addedMovie = movieRepository.save(movie);
        }
        else{
            addedMovie = movieRepository.save(completeMovie);
        }

        return addedMovie != null;
    }
    public int updateMovie(MovieDTO modifiedMovie) {
        Movie movie = this.getMovie(modifiedMovie.getId());
        if(Objects.isNull(movie)){ // If the movie is not found in the database, it cannot be updated.
            return 0;
        }
        // Update the attributes of a movie. Returns the number of successful changes.
        int numberOfChanges = 0;
        if(modifiedMovie.getName() != null && !modifiedMovie.getName().isEmpty()){
            movieRepository.setMovieNameById(modifiedMovie.getId(), modifiedMovie.getName());
            numberOfChanges++;
        }
        if(modifiedMovie.getDescription() != null) {
            movieRepository.setMovieDescriptionById(modifiedMovie.getId(), modifiedMovie.getDescription());
            numberOfChanges++;
        }
        if(modifiedMovie.getYear() != null) {
            movieRepository.setMovieYearById(modifiedMovie.getId(), modifiedMovie.getYear());
            numberOfChanges++;
        }
        if(modifiedMovie.getGenre() != null) {
            movieRepository.setMovieGenreById(modifiedMovie.getId(), modifiedMovie.getGenre());
            numberOfChanges++;
        }
        if(modifiedMovie.getDirector() != null) {
            movieRepository.setMovieDirectorById(modifiedMovie.getId(), modifiedMovie.getDirector());
            numberOfChanges++;
        }
        if(modifiedMovie.getCast() != null) {
            movieRepository.setMovieCastById(modifiedMovie.getId(), modifiedMovie.getCast());
            numberOfChanges++;
        }
        if(modifiedMovie.getScore() != null) {
            movieRepository.setMovieScoreById(modifiedMovie.getId(), modifiedMovie.getScore());
            numberOfChanges++;
        }
        if(modifiedMovie.getTrailer() != null) {
            movieRepository.setMovieTrailerById(modifiedMovie.getId(), modifiedMovie.getTrailer());
            numberOfChanges++;
        }
        if(modifiedMovie.getPoster() != null) {
            movieRepository.setMoviePosterById(modifiedMovie.getId(), modifiedMovie.getPoster());
            numberOfChanges++;
        }
        return numberOfChanges;
    }
    public Movie getMovie(Long code){
        // Returns a Movie object from its code
        Optional<Movie> found = movieRepository.findById(code);
        if(found.isPresent()){
            return found.get();
        }
        else{
            return null;
        }
    }
    public boolean removeMovie(Long code){
        // Returns true if successful, false if not.
        Movie movie = this.getMovie(code);
        if(Objects.isNull(movie)){
            return false;
        }
        else{
            movieRepository.removeById(code);
            return true;
        }
    }
}
