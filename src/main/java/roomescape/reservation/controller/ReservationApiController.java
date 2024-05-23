package roomescape.reservation.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import roomescape.auth.dto.LoginMember;
import roomescape.common.dto.ResourcesResponse;
import roomescape.reservation.domain.Status;
import roomescape.reservation.dto.request.ReservationSaveRequest;
import roomescape.reservation.dto.request.ReservationSearchCondRequest;
import roomescape.reservation.dto.response.MemberReservationResponse;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.service.ReservationService;

@RestController
public class ReservationApiController {

    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservations")
    public ResponseEntity<ResourcesResponse<ReservationResponse>> findAll() {
        List<ReservationResponse> reservations = reservationService.findAll();
        ResourcesResponse<ReservationResponse> response = new ResourcesResponse<>(reservations);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations/mine")
    public ResponseEntity<ResourcesResponse<MemberReservationResponse>> findMemberReservations(
            LoginMember loginMember
    ) {
        List<MemberReservationResponse> memberReservations = reservationService.findMemberReservations(loginMember);
        ResourcesResponse<MemberReservationResponse> response = new ResourcesResponse<>(memberReservations);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/reservations/search")
    public ResponseEntity<ResourcesResponse<ReservationResponse>> findAllBySearchCond(
            @Valid @ModelAttribute ReservationSearchCondRequest reservationSearchCondRequest
    ) {
        List<ReservationResponse> reservations = reservationService.findAllBySearchCond(reservationSearchCondRequest);
        ResourcesResponse<ReservationResponse> response = new ResourcesResponse<>(reservations);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservations")
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationSaveRequest reservationSaveRequest,
            LoginMember loginMember
    ) {
        ReservationResponse reservationResponse = reservationService.saveByLoginMember(
                reservationSaveRequest,
                loginMember,
                Status.SUCCESS
        );

        return ResponseEntity.created(URI.create("/reservations/" + reservationResponse.id()))
                .body(reservationResponse);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        reservationService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
