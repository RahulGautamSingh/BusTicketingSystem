package com.Transaction.transaction.controller;


import com.Transaction.transaction.exception.ApiResponse;
import com.Transaction.transaction.model.ReservationResponse;
import com.Transaction.transaction.payloads.BookingRequestDto;
import com.Transaction.transaction.payloads.BookingTicketDto;
import com.Transaction.transaction.service.BookingRequestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookSeats")
public class BookingRequestController {
    private final BookingRequestService bookingRequestService;

    public BookingRequestController(BookingRequestService bookingRequestService) {
        this.bookingRequestService = bookingRequestService;
    }
    @PostMapping("/{seatId}")
    public ResponseEntity<ReservationResponse> reserveSeats(@RequestBody BookingRequestDto requestDto,@PathVariable int seatId){
        ReservationResponse reservationResponse=bookingRequestService.rserveSeat(requestDto,seatId);
        return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse>  cancelSeat(@PathVariable int id){
       bookingRequestService.cancelReservation(id);
        return new ResponseEntity<>(new ApiResponse("Seat has been canceled",true,HttpStatus.OK),HttpStatus.OK);
    }
    @PostMapping("/seat/{id}/booking/{id1}")
    public ResponseEntity<ApiResponse> createBookingForSeat(@PathVariable int id, @PathVariable int id1){
        bookingRequestService.associateSeatWithBooking(id,id1);
        return new ResponseEntity<>(new ApiResponse("Seat reserved successfully ..Have  a safe journey",true,HttpStatus.CREATED),HttpStatus.CREATED);
    }
}
