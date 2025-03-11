package roomescape.reservation;

import jakarta.validation.constraints.NotNull;

public record ReservationRequest(String name,
                                 @NotNull
                                 String date,
                                 @NotNull
                                 String theme,
                                 @NotNull
                                 Long time) {
}
