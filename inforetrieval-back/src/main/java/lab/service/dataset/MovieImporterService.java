package lab.service.dataset;

import lab.domain.Movie;
import lab.service.MovieSearcherService;
import lab.utils.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Singleton;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class MovieImporterService {

    private final MovieSearcherService movieSearcherService;

    public MovieImporterService(JdbcTemplate jdbcTemplate, MovieSearcherService movieSearcherService) {
        this.movieSearcherService = movieSearcherService;
    }

    private String resourcePath = "src/main/resources/dataset/";
    private String oldDataSet = "movie_titles_old.csv";
    private String newDataSet = "movie_titles.csv";

    public BufferedReader getReadResource(String file) throws Exception {

        FileInputStream fileInput = new FileInputStream(resourcePath + file);
        return new BufferedReader(new InputStreamReader(fileInput));
    }

    public BufferedWriter getWriteResource(String file) throws Exception {

        FileOutputStream fileOutput = new FileOutputStream(resourcePath + file);
        OutputStreamWriter fileStream = new OutputStreamWriter( fileOutput);

        return new BufferedWriter(fileStream);
    }

    public void makeNewDataSet() throws Exception {

        BufferedWriter writer = getWriteResource(newDataSet);
        BufferedReader reader = getReadResource(oldDataSet);
        reader.readLine();

        writer.write("id;name;year");
        writer.newLine();

        String line;

        while ((line = reader.readLine()) != null) {
            String[] splited = line.split(",");
            String[] newRow = new String[3];
            newRow[0] = StringUtils.toInteger(splited[0]).toString();
            newRow[1] = splited[1].replaceAll("\\((\\d{4})\\)", "").trim();
            newRow[2] = splited[1].replaceAll(".*\\((\\d{4})\\).*", "$1");


            writer.write(String.join(";", newRow));
            writer.newLine();
        }

        reader.close();
        writer.close();
    }

    public void importMovies() throws Exception {

        BufferedReader reader = getReadResource(newDataSet);
        reader.readLine();

        List<Movie> listOfMovies = new ArrayList<Movie>();
        String line;

        while ((line = reader.readLine()) != null) {
            String[] splited = line.split(";");
            Long id = StringUtils.toLong(splited[0]);
            String name = splited[1];
            Long year = StringUtils.toLong(splited[2]);

            listOfMovies.add(new Movie(id, name, year));
        }

        reader.close();

        int[][] batch = movieSearcherService.batchInsert(listOfMovies, 50);
        System.out.println(batch.toString());

    }
}
