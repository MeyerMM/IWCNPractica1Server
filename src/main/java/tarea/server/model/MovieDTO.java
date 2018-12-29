package tarea.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@ApiModel(description = "Class that represents a movie")
public class MovieDTO {
    @ApiModelProperty(notes = "Unique id that identifies the movie.", example = "\"3\"")
    private Long id;
    @ApiModelProperty(notes = "Name of the movie", example = "It")
    private String name;
    @ApiModelProperty(notes = "Description of the movie", example = "In 1960, seven pre-teen outcasts fight an evil " +
            "demon who poses as a child-killing clown. Thirty years later, they reunite to stop the demon once and for " +
            "all when it returns to their hometown.")
    private String description;
    @ApiModelProperty(notes = "Year of release of the movie", example = "2017")
    private Integer year;
    @ApiModelProperty(notes = "Genre of the movie", example = " Drama, Fantasy, Thriller ")
    private String genre;
    @ApiModelProperty(notes = "Director of the movie", example = "Andy Muschietti")
    private String director;
    @ApiModelProperty(notes = "Cast of the movie", example = "Bill Skarsgård, Jaeden Lieberher, Finn Wolfhard")
    private String cast;
    @ApiModelProperty(notes = "Score from 0 to 5 of the movie", example = "4.7")
    private Float score;
    @ApiModelProperty(notes = "URL to the youtube trailer of the movie", example = "https://youtu.be/xKJmEC5ieOk")
    private String trailer;
    @ApiModelProperty(notes = "URL to the poster of the movie", example = "https://m.media-amazon.com/images/M/MV5BZDVkZmI0YzAtNzdjYi00ZjhhLWE1ODEtMWMzMWMzNDA0NmQ4XkEyXkFqcGdeQXVyNzYzODM3Mzg@._V1_SY1000_CR0,0,666,1000_AL_.jpg")
    private String poster;

    public MovieDTO() {
    }

    public MovieDTO(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}