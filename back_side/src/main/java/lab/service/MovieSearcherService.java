package lab.service;

import lab.domain.Movie;

import io.micronaut.context.annotation.Requires;
import io.micronaut.spring.tx.annotation.Transactional;
import lab.dto.MovieSearchDto;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import javax.inject.Singleton;
import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Requires(beans = JdbcTemplate.class)
public class MovieSearcherService {

    private JdbcTemplate jdbcTemplate;

    public static String DATE_FORAMT = "([12][0-9]{3})";

    public MovieSearcherService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Movie> searchMoviesByParams(MovieSearchDto params) {

        String query = "SELECT * FROM movies ";
        ArrayList<Object> objects = new ArrayList<Object>();

        ///////////////////
        String searchString = params.getSearchString().trim();

        if (searchString != null && !searchString.equals("")) {

            String searchDate = searchString.replaceAll("[^0-9]{1,4}", "").trim();
            String searchName = searchString;

            Boolean isDateMatch = false;

            if (searchDate.matches(DATE_FORAMT)) {
                isDateMatch = true;
                searchName = searchName.replaceAll(DATE_FORAMT, "").trim();
            }

            query += "WHERE ";
            query += _splitSearchStringToQuery(objects, searchName);

            if (isDateMatch) {
                query += "AND CAST(year AS TEXT) ILIKE ? ";
                objects.add(searchDate + "%");
            }

        }

        ///////////////////
        String orderBy = params.getOrderBy();
        String orderField = params.getOrderField();

        if (orderBy == null || orderBy.equals("")) {
            orderBy = "asc";
        }

        if (orderField == null || orderField.equals("")) {
            orderField = "year";
        }

        query += "ORDER BY " + orderField + " " + orderBy;

        ///////////////////
        if (params.getMax() == null) {
            params.setMax(10);
        }

        query += " LIMIT ?";
        objects.add(params.getMax());

        ///////////////////
        if (params.getOffset() == null) {
            params.setOffset(0);
        }

        query += " OFFSET ?";
        objects.add(params.getOffset());

        System.out.print(query);
        System.out.print("  " + objects.toString() + "\n");
        List<Movie> movies = this.jdbcTemplate.query(query, objects.toArray(),
                new RowMapper<Movie>() {
                    @Override
                    public Movie mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Movie movie = new Movie();
                        movie.setId(rs.getLong("id"));
                        movie.setYear(rs.getLong("year"));
                        movie.setName(rs.getString("name"));

                        return movie;
                    }
                }
        );

        return movies;
    }

    private String _splitSearchStringToQuery(ArrayList<Object> objects, String searchString) {

        String query = "";
        String[] splitedString = searchString.split(" ");
        int size = splitedString.length;

        String condition = "AND ";

        for (int i = 0; i < size; i++) {

            String str = splitedString[i];

            if (i == size - 1) {
                condition = "";
            }

            query += "name ILIKE ? " + condition;
            objects.add("%" + str + "%");
        }

        return query;
    }


    public void clearMoviesTable() {
        jdbcTemplate.execute("DELETE FROM movies");
    }

//    @Transactional
    public void batchInsert(List<Movie> movies, int batchSize) throws Exception {

        for (int j = 0; j < movies.size(); j += batchSize) {

            final List<Movie> batchList = movies.subList(j, Math.min(j + batchSize, movies.size()));

            jdbcTemplate.batchUpdate("INSERT INTO movies (id, name, year) VALUES(?, ?, ?) ON CONFLICT DO NOTHING",
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Movie movie = batchList.get(i);
                            ps.setLong(1, movie.getId());
                            ps.setString(2, movie.getName());
                            ps.setLong(3, movie.getYear());
                        }

                        @Override
                        public int getBatchSize() {
                            return batchList.size();
                        }
                    }
            );

        }

    }
}
