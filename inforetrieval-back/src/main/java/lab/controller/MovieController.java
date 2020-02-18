package lab.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import lab.service.dataset.MovieImporterService;

@Controller("/")
public class MovieController {

    protected final MovieImporterService movieImporterService;

    public MovieController(MovieImporterService movieImporterService) {
        this.movieImporterService = movieImporterService;
    }

    @Get
    @Produces(MediaType.APPLICATION_JSON)
    public String index() {
        return "Hello World";
    }

    @Get(value = "/import", produces = MediaType.APPLICATION_JSON)
    public String importMovies() {
        try {
            this.movieImporterService.makeNewDataSet();
            this.movieImporterService.importMovies();
        } catch (Exception exception) {
            return exception.getMessage();
        }
        return "Imported";
    }
}


