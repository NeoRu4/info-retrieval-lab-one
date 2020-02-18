package lab.service;

import lab.domain.Movie;

import io.micronaut.context.annotation.Requires;
import io.micronaut.spring.tx.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import javax.inject.Singleton;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Singleton
@Requires(beans = JdbcTemplate.class)
public class MovieSearcherService {

    private JdbcTemplate jdbcTemplate;

    public MovieSearcherService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Transactional
    public int[][] batchInsert(List<Movie> movies, int batchSize) {
        int[][] updateCounts = jdbcTemplate.batchUpdate(
                "INSERT INTO movies (id, name, year) VALUES(?, ?, ?) ON CONFLICT DO UPDATE",
                movies,
                batchSize,
                new ParameterizedPreparedStatementSetter<Movie>() {
                    public void setValues(PreparedStatement ps, Movie argument) throws SQLException {
                        ps.setLong(1, argument.getId());
                        ps.setString(2, argument.getName());
                        ps.setLong(3, argument.getYear());
                    }
                });
        return updateCounts;

    }
}
