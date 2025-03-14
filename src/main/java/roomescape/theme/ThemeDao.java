package roomescape.theme;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;
import roomescape.global.exception.RoomescapeNotFoundException;

@Repository
public class ThemeDao {

    public static final RowMapper<Theme> THEME_ROW_MAPPER = (resultSet, rowNum) ->
            new Theme(
                    resultSet.getLong("id"),
                    resultSet.getObject("theme_name", String.class),
                    resultSet.getObject("description", String.class)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ThemeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("theme")
                .usingGeneratedKeyColumns("id");
    }

    public List<Theme> findAll() {
        String sql = "select * from theme where deleted = false";

        return jdbcTemplate.query(sql, THEME_ROW_MAPPER);
    }

    public Theme findById(long id) {
        try {
            String sql = "select * from theme where id = ?";

            return jdbcTemplate.queryForObject(sql, THEME_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException exception) {
            throw new RoomescapeNotFoundException("예약 시간을 찾을 수 없습니다. id: " + id);
        }
    }

    public Theme save(Theme theme) {
        Map<String, Object> parameters = Map.of(
                "name", theme.getName(),
                "description", theme.getDescription());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters)
                .longValue();

        return new Theme(id, theme.getName(), theme.getDescription());
    }

    public void deleteById(Long id) {
        String sql = "UPDATE theme SET deleted = true WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }
}
