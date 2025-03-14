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
                    resultSet.getLong("id"),
                    resultSet.getString("name"),
                    resultSet.getObject("date", LocalDate.class),
                    new ReservationTime(
                            resultSet.getLong("id"),
                            resultSet.getObject("time_value", LocalTime.class)
                    ),
                    new Theme(
                            resultSet.getLong("id"),
                            resultSet.getString("theme_name"),
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
        String sql = "SELECT * " +
                "FROM reservation AS r " +
                "INNER JOIN reservationTime AS rt ON r.time_id = rt.id " +
                "INNER JOIN theme AS th ON r.theme_id = th.id";

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER);
    }

    public Reservation save(Reservation reservation) {
        Map<String, Object> parameters = Map.of(
                "name", reservation.getName(),
                "date", reservation.getDate(),
                "time_id", reservation.getTime().getId(),
                "theme_id", reservation.getTheme().getId()
        );
        Long id = simpleJdbcInsert.executeAndReturnKey(parameters)
                .longValue();

        return new Reservation(id, reservation.getName(), reservation.getDate(), reservation.getTime(),
                reservation.getTheme());
    }

    public void deleteById(Long id) {
        String sql = "delete from reservation where id = ?";

        jdbcTemplate.update(sql, id);
    }

    public List<Reservation> findAllReservationsByDateAndTheme(String date, Long themeId) {
        String sql = "select * from reservation where date = ? and theme_id = ?";

        return jdbcTemplate.query(sql, RESERVATION_ROW_MAPPER, date, themeId);
    }
}
