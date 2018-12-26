package tarea.server.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import tarea.server.repositories.MovieRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class MovieService implements MovieList {
    private MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
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
        MovieSpecifications nameSpec = new MovieSpecifications(new SearchCriteria("code", operationContains, ""));
        MovieSpecifications genreSpec = new MovieSpecifications(new SearchCriteria("code", operationContains, ""));
        MovieSpecifications directorSpec = new MovieSpecifications(new SearchCriteria("code", operationContains, ""));
        MovieSpecifications castSpec = new MovieSpecifications(new SearchCriteria("code", operationContains, ""));
        MovieSpecifications yearSpec = new MovieSpecifications(new SearchCriteria("code", operationContains, ""));
        MovieSpecifications scoreSpec = new MovieSpecifications(new SearchCriteria("code", operationContains, ""));


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
        // Adds the movie received as an object. Returns true if successful, false if not
        Movie addedMovie = movieRepository.save(movie);
        return addedMovie != null;
    }
    public int updateMovie(Movie modifiedMovie) {
        Movie movie = this.getMovie(modifiedMovie.getCode());
        if(Objects.isNull(movie)){ // If the movie is not found in the database, it cannot be updated.
            return 0;
        }
        // Update the attributes of a movie. Returns the number of successful changes.
        int numberOfChanges = 0;
        if(modifiedMovie.getName() != null && !modifiedMovie.getName().isEmpty()){
            movieRepository.setMovieNameByCode(modifiedMovie.getCode(), modifiedMovie.getName());
            numberOfChanges++;
        }
        if(modifiedMovie.getDescription() != null) {
            movieRepository.setMovieDescriptionByCode(modifiedMovie.getCode(), modifiedMovie.getDescription());
            numberOfChanges++;
        }
        if(modifiedMovie.getYear() != null) {
            movieRepository.setMovieYearByCode(modifiedMovie.getCode(), modifiedMovie.getYear());
            numberOfChanges++;
        }
        if(modifiedMovie.getGenre() != null) {
            movieRepository.setMovieGenreByCode(modifiedMovie.getCode(), modifiedMovie.getGenre());
            numberOfChanges++;
        }
        if(modifiedMovie.getDirector() != null) {
            movieRepository.setMovieDirectorByCode(modifiedMovie.getCode(), modifiedMovie.getDirector());
            numberOfChanges++;
        }
        if(modifiedMovie.getCast() != null) {
            movieRepository.setMovieCastByCode(modifiedMovie.getCode(), modifiedMovie.getCast());
            numberOfChanges++;
        }
        if(modifiedMovie.getScore() != null) {
            movieRepository.setMovieScoreByCode(modifiedMovie.getCode(), modifiedMovie.getScore());
            numberOfChanges++;
        }
        if(modifiedMovie.getTrailer() != null) {
            movieRepository.setMovieTrailerByCode(modifiedMovie.getCode(), modifiedMovie.getTrailer());
            numberOfChanges++;
        }
        if(modifiedMovie.getPoster() != null) {
            movieRepository.setMoviePosterByCode(modifiedMovie.getCode(), modifiedMovie.getPoster());
            numberOfChanges++;
        }
        return numberOfChanges;
    }
    public Movie getMovie(String code){
        // Returns a Movie object from its code
        return movieRepository.findByCode(code);
    }
    public boolean removeMovie(String code){
        // Returns true if successful, false if not.
        Movie movie = this.getMovie(code);
        if(Objects.isNull(movie)){
            return false;
        }
        else{
            movieRepository.removeByCode(code);
            return true;
        }
    }
}
