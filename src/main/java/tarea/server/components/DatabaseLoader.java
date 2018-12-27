package tarea.server.components;

import tarea.server.model.Movie;
import tarea.server.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseLoader {
    private MovieRepository movieRepository;

    @Autowired
    public DatabaseLoader(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @PostConstruct
    public void initDatabase(){
        Movie movie = new Movie("Get Out");
        movie.setId(Integer.toUnsignedLong(1)); //Fix the ID of the initialization movie so it is not created again every time the program is executed.
        movie.setDescription("A young African-American visits his white girlfriend's parents for the weekend, " +
                "where his simmering uneasiness about their reception of him eventually reaches a boiling point.");
        movie.setGenre("Horror, Mystery, Thriller");
        movie.setDirector("Jordan Peele");
        movie.setCast("Daniel Kaluuya, Allison Williams, Bradley Whitford");
        movie.setYear(2017);
        movie.setScore((float)3.8);
        movie.setTrailer("https://youtu.be/DzfpyUB60YY");
        movie.setPoster("https://m.media-amazon.com/images/M/MV5BMjUxMDQwNjcyNl5BMl5BanBnXkFtZTgwNzcwMzc0MTI@._V1_SY1000_CR0,0,675,1000_AL_.jpg");
        movieRepository.save(movie);
    }

}

