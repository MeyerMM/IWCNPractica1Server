package tarea.server.externalapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tarea.server.controller.MovieController;
import tarea.server.model.Movie;


import java.util.Objects;

@Component
public class ExternalAPIHandler {
    private RestTemplate restTemplate;
    private String oMDbRestServerUrl; // Rest server from which to get the text data on the movie
    private String theMovieDBRestServerUrl; // Rest server to get the Poster and Trailer for the movie
    private String theMovieDBGetMoviePath;
    private String theMovieDBGetVideoPath;
    private String theMovieDBAccessKey;
    private String theMovieDBSource;
    private String posterBase;
    private String videoBase;

    private Logger logger = LoggerFactory.getLogger(MovieController.class);

    @Autowired
    public ExternalAPIHandler(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        oMDbRestServerUrl = "http://www.omdbapi.com/?apikey=a47834c";
        theMovieDBRestServerUrl = "https://api.themoviedb.org/3/";
        theMovieDBGetMoviePath = "find/";
        theMovieDBGetVideoPath = "movie/";
        theMovieDBAccessKey = "api_key=c49a8abb94461d2f60632418dc3e0300";
        theMovieDBSource = "external_source=imdb_id";
        posterBase = "https://image.tmdb.org/t/p/w1280";
        videoBase = "https://www.youtube.com/embed/";
    }

    public OMDbMovie getMovieFromOMDb(Movie wantedMovie){
        String requestUrl = oMDbRestServerUrl + "&type=movie";
        if(!Objects.isNull(wantedMovie.getName()) && !wantedMovie.getName().isEmpty()){
            requestUrl = requestUrl + "&t=" + wantedMovie.getName();
            if(!Objects.isNull(wantedMovie.getYear())){
                requestUrl = requestUrl + "&y=" + wantedMovie.getYear();
            }


            OMDbMovie foundMovie = restTemplate.getForObject(requestUrl, OMDbMovie.class);
            logger.info(String.format("Request made to OMDb: %s", requestUrl));
            if(foundMovie.getResponse().equals("True")){
                logger.info("Request to OMDb was successful");
                return foundMovie;
            }
            else{
                logger.info("Request to OMDb failed");
                return null;
            }
        }
        else{
            return null;
        }
    }

    public Movie completeMovieText(Movie wantedMovie, OMDbMovie foundMovie){
        if(!Objects.isNull(foundMovie)){
            if(Objects.isNull(wantedMovie.getDescription()) || wantedMovie.getDescription().isEmpty()){
                wantedMovie.setDescription(foundMovie.getPlot());
            }
            if(Objects.isNull(wantedMovie.getYear())){
                if(foundMovie.getYear().equals("N/A")){
                    wantedMovie.setYear(0); // If year is not available yet, set as 0
                }
                else{
                    wantedMovie.setYear(Integer.parseUnsignedInt(foundMovie.getYear()));
                }
            }
            if(Objects.isNull(wantedMovie.getGenre()) || wantedMovie.getGenre().isEmpty()){
                wantedMovie.setGenre(foundMovie.getGenre());
            }
            if(Objects.isNull(wantedMovie.getDirector()) || wantedMovie.getDirector().isEmpty()){
                wantedMovie.setDirector(foundMovie.getDirector());
            }
            if(Objects.isNull(wantedMovie.getCast()) || wantedMovie.getCast().isEmpty()){
                wantedMovie.setCast(foundMovie.getActors());
            }
            if(Objects.isNull(wantedMovie.getScore())){
                if(foundMovie.getMetascore().equals("N/A")){
                    wantedMovie.setScore((float)0); // If metascore is not available yet, rate as 0
                }
                else{
                    wantedMovie.setScore(Float.parseFloat(foundMovie.getMetascore())/20); // Metascore is up to 100. Application score is up to 5.
                }
            }
            if(Objects.isNull(wantedMovie.getPoster()) || wantedMovie.getPoster().isEmpty()){
                wantedMovie.setPoster(foundMovie.getPoster());
            }
            return wantedMovie;
        }
        else{
            return null;
        }
    }

    public TheMovieDBMovieResults getMovieFromTheMovieDB(String imdbID){
        if(Objects.isNull(imdbID) || imdbID.isEmpty()){
            logger.info("Request to TheMovieDB failed due to lacking IMDB ID");
            return null;
        }
        else{
            String requestUrl = theMovieDBRestServerUrl+theMovieDBGetMoviePath+imdbID+"?"+theMovieDBAccessKey+"&"+theMovieDBSource;
            logger.info(String.format("Request made to TheMovieDB: %s", requestUrl));
            TheMovieDBMovieResults[] moviesFound = restTemplate.getForObject(requestUrl, TheMovieDBFindResponse.class).getMovieResults();
            if(Objects.isNull(moviesFound) || moviesFound.length == 0 ){
                logger.info("Request to TheMovieDB failed");
                return null;
            }
            else{
                logger.info("Request to TheMovieDB was successful");
                return moviesFound[0];
            }
        }
    }

    public TheMovieDBVideoResult getVideoFromTheMovieDB(String movieId){
        String requestUrl = theMovieDBRestServerUrl+theMovieDBGetVideoPath + movieId + "/videos?" + theMovieDBAccessKey;
        logger.info(String.format("Request made to TheMovieDB for a video: %s", requestUrl));
        TheMovieDBVideoResult[] videoArray = restTemplate.getForObject(requestUrl, TheMovieDBVideosResponse.class).getResults();
        if(Objects.isNull(videoArray) || videoArray.length == 0 ||  Objects.isNull(videoArray[0])){
            logger.info("Request to TheMovieDB for a video failed");
            return null;
        }
        else{
            logger.info("Request to TheMovieDB for a video was successful");
            return videoArray[0];
        }
    }

    public Movie completeMovie(Movie wantedMovie){
        OMDbMovie oMDBMovie =  getMovieFromOMDb(wantedMovie);
        Movie updatedMovie = completeMovieText(wantedMovie, oMDBMovie);
        if(!Objects.isNull(updatedMovie)){   // If the movie was found in oMDB
            if(!Objects.isNull(oMDBMovie.getImdbID()) && !oMDBMovie.getImdbID().isEmpty()){ // If the result from oMDB has the IMDB Id used to access TheMovieDB
                TheMovieDBMovieResults theMovieDBMovie = getMovieFromTheMovieDB(oMDBMovie.getImdbID());
                if(!Objects.isNull(theMovieDBMovie)){               // If the movie was found in TheMovieDB
                    if(!Objects.isNull(theMovieDBMovie.getPosterPath()) && !theMovieDBMovie.getPosterPath().isEmpty()){ // If a poster was found in the results of TheMovieDB
                        wantedMovie.setPoster(posterBase + theMovieDBMovie.getPosterPath());
                    }
                    if(!Objects.isNull(theMovieDBMovie.getId()) && !theMovieDBMovie.getId().isEmpty()){ // If the movie Id used to get the trailer was found in the results of TheMovieDB
                        TheMovieDBVideoResult foundVideo = getVideoFromTheMovieDB(theMovieDBMovie.getId());
                        if(!Objects.isNull(foundVideo) && !Objects.isNull(foundVideo.getKey()) && !foundVideo.getKey().isEmpty()){   // If the key for the video was found
                            wantedMovie.setTrailer(videoBase + foundVideo.getKey());
                        }
                    }
                }
            }

            return wantedMovie;
        }
        else{
            return null;
        }
    }

}
