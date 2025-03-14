package roomescape.reservationTime;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import roomescape.global.exception.RoomescapeNotFoundException;

@Repository
public class ReservationTimeDao {

    public static final RowMapper<ReservationTime> TIME_ROW_MAPPER = (resultSet, rowNum) ->
            new ReservationTime(
                    resultSet.getLong("id"),
                    resultSet.getObject("time_value", LocalTime.class)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).withTableName("time")
                .usingGeneratedKeyColumns("id");
    }

    public List<ReservationTime> findAll() {
        String sql = "SELECT * FROM reservationTime WHERE deleted = false";
        return jdbcTemplate.query(sql, TIME_ROW_MAPPER);
    }

    public ReservationTime findById(Long id) {
        try {
            String sql = "SELECT * FROM reservationTime WHERE id = ?";
            return jdbcTemplate.queryForObject(sql, TIME_ROW_MAPPER, id);
        } catch (EmptyResultDataAccessException e) {
            throw new RoomescapeNotFoundException("예약 시간을 찾을 수 없습니다. id: " + id);
        }
    }

    public ReservationTime save(ReservationTime reservationTime) {
        Map<String, Object> parameters = Map.of("time_value", reservationTime.getValue());
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
        return new ReservationTime(id, reservationTime.getValue());
    }

    public void deleteById(Long id) {
        String sql = "UPDATE reservationTime SET deleted = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
