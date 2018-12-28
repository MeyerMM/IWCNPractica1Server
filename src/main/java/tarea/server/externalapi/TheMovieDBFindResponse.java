package tarea.server.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheMovieDBFindResponse {
    @JsonProperty("movie_results")
    private TheMovieDBMovieResults[] movieResults;

    public TheMovieDBMovieResults[] getMovieResults() {
        return movieResults;
    }

    public void setMovieResults(TheMovieDBMovieResults[] movieResults) {
        this.movieResults = movieResults;
    }
}
