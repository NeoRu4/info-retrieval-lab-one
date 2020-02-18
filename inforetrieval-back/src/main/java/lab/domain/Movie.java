package lab.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

//@Table(name= "movie")
public class Movie {

    public Movie(Long id, @NotNull String name, @NotNull Long year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @NotNull
    @Column(name = "year", nullable = false, unique = false)
    private Long year;


    public Long getYear() {
        return year;
    }

    public void setYear(Long year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
