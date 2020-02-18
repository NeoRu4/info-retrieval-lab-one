package lab.service.dataset;

import io.micronaut.context.annotation.Requires;
import lab.utils.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Singleton;

import java.io.*;

@Singleton
@Requires(beans = JdbcTemplate.class)
public class MovieImporterService {

    private final JdbcTemplate jdbcTemplate;

    public MovieImporterService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

        StringBuilder content = new StringBuilder();
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

    public void importMovies() {

    }
}
