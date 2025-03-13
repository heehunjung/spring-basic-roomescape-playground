package roomescape.reservationTime;

import org.springframework.stereotype.Service;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;

import java.util.List;

@Service
public class TimeService {

    private TimeDao timeDao;
    private ReservationDao reservationDao;

    public TimeService(TimeDao timeDao, ReservationDao reservationDao) {
        this.timeDao = timeDao;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findAllReservationsByDateAndTheme(date, themeId);
        List<ReservationTime> reservationTimes = timeDao.findAll();

        return reservationTimes.stream()
                .map(time -> new AvailableTime(
                        time.getId(),
                        time.getValue().toString(),
                        reservations.stream()
                                .anyMatch(reservation -> reservation.getTime().getId().equals(time.getId()))
                ))
                .toList();
    }

    public List<ReservationTime> findAll() {
        return timeDao.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        return timeDao.save(reservationTime);
    }

    public void deleteById(Long id) {
        timeDao.deleteById(id);
    }
}
