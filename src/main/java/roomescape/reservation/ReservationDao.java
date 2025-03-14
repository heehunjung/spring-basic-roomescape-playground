package roomescape.reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.theme.Theme;
import roomescape.reservationTime.ReservationTime;

import java.util.List;

@Repository
public class ReservationDao {

    private static final RowMapper<Reservation> RESERVATION_ROW_MAPPER = (resultSet, rowNum) ->
            new Reservation(
                    resultSet.getLong("reservation_id"),
                    resultSet.getString("name"),
                    resultSet.getObject("date", LocalDate.class),
                    new ReservationTime(
                            resultSet.getLong("time_id"),
                            resultSet.getObject("time_value", LocalTime.class)
                    ),
                    new Theme(
                            resultSet.getLong("theme_id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    )
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
    }

    public List<Reservation> findAll() {
        String sql = "select * from reservation";

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time", reservation.getTime(),
                "theme", reservation.getTheme()
        );
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters)
                .longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where reservation_id = ?";

        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findAllReservationsByDateAndTheme(String date, Long themeId) {
        String sql = "select * from reservation where date = ? and theme_id = ?";

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, date, themeId);
    }
}
