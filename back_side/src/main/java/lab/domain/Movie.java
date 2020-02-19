package lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lab.dto.Validatable;

import javax.validation.constraints.NotNull;

//@Table(name= "movie")
public class Movie implements Validatable {

    public Movie() {}

    public Movie(Long id, @NotNull String name, @NotNull Long year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private Long year;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @JsonIgnore
    public Boolean isValid() {
        return  getId() != null &&
                getName() != null &&
                getYear() != null;
    }
}
