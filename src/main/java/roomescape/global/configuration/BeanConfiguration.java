package roomescape.global.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;


@Configuration
public class BeanConfiguration {

    private final JdbcTemplate jdbcTemplate;

    public BeanConfiguration(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Bean
    public SimpleJdbcInsert SimpleJdbcInsert() {
        return new SimpleJdbcInsert(jdbcTemplate);
    }
}
