package tarea.server.externalapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TheMovieDBVideosResponse {
    @JsonProperty("results")
    private TheMovieDBVideoResult[] results;

    public TheMovieDBVideoResult[] getResults() {
        return results;
    }

    public void setResults(TheMovieDBVideoResult[] results) {
        this.results = results;
    }
}
