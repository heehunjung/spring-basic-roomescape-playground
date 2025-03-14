package roomescape.reservation;

import org.springframework.stereotype.Service;

import java.util.List;
import roomescape.theme.Theme;
import roomescape.theme.ThemeDao;
import roomescape.reservationTime.ReservationTime;
import roomescape.reservationTime.ReservationTimeDao;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ThemeDao themeDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationService(ReservationDao reservationDao, ThemeDao themeDao, final ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.themeDao = themeDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationResponse save(ReservationRequest reservationRequest) {
        Theme theme = themeDao.findById(reservationRequest.theme());
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.time());
        Reservation reservation = reservationDao.save(reservationRequest.toReservation(theme, reservationTime));
        System.out.println("reservation = " + reservation);
        System.out.println("theme = " + theme);
        return new ReservationResponse(reservation);
    }

    public void deleteById(Long id) {
        reservationDao.deleteById(id);
    }

    public List<ReservationResponse> findAll() {
        return reservationDao.findAll().stream()
                .map(it -> new ReservationResponse(it))
                .toList();
    }
}
