package lab.dataset;

import lab.utils.StringUtils;
import org.springframework.stereotype.Component;

import java.io.*;

public class MovieImporterSingleton {

    private static MovieImporterSingleton movieImporterSingleton;

    private MovieImporterSingleton() { }

    public static MovieImporterSingleton getInstance()
    {
        if (movieImporterSingleton == null) {
            movieImporterSingleton = new MovieImporterSingleton();
        }
        return movieImporterSingleton;
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

    public void makeNewDataSet()  throws Exception {

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
