package com.Transaction.transaction.controller;

import com.Transaction.transaction.entity.BookingRequest;
import com.Transaction.transaction.exception.ApiResponse;
import com.Transaction.transaction.model.ReservationResponse;
import com.Transaction.transaction.payloads.BookingRequestDto;
import com.Transaction.transaction.payloads.ReservationDto;
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

    @PostMapping("/")
    public ResponseEntity<ReservationResponse> reserveSeats(@RequestBody BookingRequestDto requestDto){
        ReservationResponse reservationResponse=bookingRequestService.rserveSeats(requestDto);
        return new ResponseEntity<>(reservationResponse, HttpStatus.CREATED);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse>  cancelSeat(@PathVariable int id){
       bookingRequestService.cancelReservation(id);
        return new ResponseEntity<>(new ApiResponse("Seat Has Been Canceled",true,HttpStatus.OK),HttpStatus.OK);
    }
}
