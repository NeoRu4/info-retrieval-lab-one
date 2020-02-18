package lab.service;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Singleton;

@Singleton
public class MovieSearcherService {

    private JdbcTemplate jdbcTemplate;

    public MovieSearcherService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


}
