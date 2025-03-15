package roomescape.reservationTime;

import org.springframework.stereotype.Service;
import roomescape.global.exception.RoomescapeBadRequestException;
import roomescape.reservation.Reservation;
import roomescape.reservation.ReservationDao;

import java.util.List;

@Service
public class ReservationTimeService {

    private ReservationTimeDao reservationTimeDao;
    private ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<AvailableTime> getAvailableTime(String date, Long themeId) {
        List<Reservation> reservations = reservationDao.findAllReservationsByDateAndTheme(date, themeId);
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();

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
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(ReservationTime reservationTime) {
        if (reservationTime.getId() == null || reservationTime.getValue() == null) {
            throw new RoomescapeBadRequestException("잘못된 예약 시간 정보입니다.");
        }
        return reservationTimeDao.save(reservationTime);
    }

    public void deleteById(Long id) {
        reservationTimeDao.deleteById(id);
    }
}
