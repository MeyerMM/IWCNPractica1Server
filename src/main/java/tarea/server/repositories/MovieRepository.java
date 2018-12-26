package tarea.server.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tarea.server.model.Movie;

import javax.transaction.Transactional;
import java.util.List;

public interface MovieRepository extends CrudRepository<Movie, String>, JpaSpecificationExecutor {
    List<Movie> findAll();

    Movie findByCode(String code);

    @Transactional
    Movie save(Movie movie);

    @Transactional
    void removeByCode(String code);

    @Transactional
    @Modifying
    @Query("update Movie m set m.name = ?2 where m.code = ?1")
    void setMovieNameByCode(String code, String name);

    @Transactional
    @Modifying
    @Query("update Movie m set m.description = ?2 where m.code = ?1")
    void setMovieDescriptionByCode(String code, String description);

    @Transactional
    @Modifying
    @Query("update Movie m set m.year = ?2 where m.code = ?1")
    void setMovieYearByCode(String code, int year);

    @Transactional
    @Modifying
    @Query("update Movie m set m.genre = ?2 where m.code = ?1")
    void setMovieGenreByCode(String code, String genre);

    @Transactional
    @Modifying
    @Query("update Movie m set m.director = ?2 where m.code = ?1")
    void setMovieDirectorByCode(String code, String director);

    @Transactional
    @Modifying
    @Query("update Movie m set m.cast = ?2 where m.code = ?1")
    void setMovieCastByCode(String code, String cast);

    @Transactional
    @Modifying
    @Query("update Movie m set m.score = ?2 where m.code = ?1")
    void setMovieScoreByCode(String code, float score);

    @Transactional
    @Modifying
    @Query("update Movie m set m.trailer = ?2 where m.code = ?1")
    void setMovieTrailerByCode(String code, String trailer);

    @Transactional
    @Modifying
    @Query("update Movie m set m.poster = ?2 where m.code = ?1")
    void setMoviePosterByCode(String code, String poster);
}

