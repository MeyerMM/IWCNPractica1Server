package tarea.server.repositories;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import tarea.server.model.Movie;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface MovieRepository extends CrudRepository<Movie, Long>, JpaSpecificationExecutor {
    List<Movie> findAll();

    Optional<Movie> findById(Long id);

    @Transactional
    Movie save(Movie movie);

    @Transactional
    void removeById(Long id);

    @Transactional
    @Modifying
    @Query("update Movie m set m.name = ?2 where m.id = ?1")
    void setMovieNameById(Long id, String name);

    @Transactional
    @Modifying
    @Query("update Movie m set m.description = ?2 where m.id = ?1")
    void setMovieDescriptionById(Long id, String description);

    @Transactional
    @Modifying
    @Query("update Movie m set m.year = ?2 where m.id = ?1")
    void setMovieYearById(Long id, int year);

    @Transactional
    @Modifying
    @Query("update Movie m set m.genre = ?2 where m.id = ?1")
    void setMovieGenreById(Long id, String genre);

    @Transactional
    @Modifying
    @Query("update Movie m set m.director = ?2 where m.id = ?1")
    void setMovieDirectorById(Long id, String director);

    @Transactional
    @Modifying
    @Query("update Movie m set m.cast = ?2 where m.id = ?1")
    void setMovieCastById(Long id, String cast);

    @Transactional
    @Modifying
    @Query("update Movie m set m.score = ?2 where m.id = ?1")
    void setMovieScoreById(Long id, float score);

    @Transactional
    @Modifying
    @Query("update Movie m set m.trailer = ?2 where m.id = ?1")
    void setMovieTrailerById(Long id, String trailer);

    @Transactional
    @Modifying
    @Query("update Movie m set m.poster = ?2 where m.id = ?1")
    void setMoviePosterById(Long id, String poster);
}

