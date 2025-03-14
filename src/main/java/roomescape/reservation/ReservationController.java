package roomescape.reservation;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import roomescape.auth.AuthMember;
import roomescape.member.Member;

@RestController
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public List<ReservationResponse> list() {
        return reservationService.findAll();
    }

    @PostMapping("/reservations")
    public ResponseEntity create(@AuthMember Member member, @RequestBody ReservationRequest reservationRequest) {
        String name = reservationRequest.name();
        if (name == null || name.isEmpty()) {
            name = member.getName();
            System.out.println("name = " + name);
            reservationRequest = reservationRequest.update(name);
        }
        ReservationResponse result = reservationService.save(reservationRequest);

        return ResponseEntity.created(URI.create("/reservations/" + result.id()))
                .body(result);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        reservationService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
