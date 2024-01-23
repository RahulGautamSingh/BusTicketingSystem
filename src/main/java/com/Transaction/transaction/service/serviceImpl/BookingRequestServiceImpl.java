package com.Transaction.transaction.service.serviceImpl;

import com.Transaction.transaction.entity.BookingRequest;
import com.Transaction.transaction.entity.Seat;
import com.Transaction.transaction.exception.BookingNotFoundException;
import com.Transaction.transaction.exception.ResourceNotFoundException;
import com.Transaction.transaction.exception.SeatsNotAvailableException;
import com.Transaction.transaction.model.ReservationResponse;
import com.Transaction.transaction.payloads.BookingRequestDto;
import com.Transaction.transaction.repository.BookingRequestRepo;
import com.Transaction.transaction.repository.BookingSeatsRepo;
import com.Transaction.transaction.repository.SeatRepo;
import com.Transaction.transaction.service.BookingRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public  class BookingRequestServiceImpl implements BookingRequestService {
    private final SeatRepo seatRepo;
//    private final BookingRequestRepo requestRepo;
    private final ModelMapper modelMapper;
    private final BookingSeatsRepo bookingSeatsRepo;
    private final BookingRequestRepo requestRepo;

    public BookingRequestServiceImpl(SeatRepo seatRepo, ModelMapper modelMapper, BookingSeatsRepo bookingSeatsRepo, BookingRequestRepo requestRepo) {
        this.seatRepo = seatRepo;
        this.modelMapper = modelMapper;
        this.bookingSeatsRepo = bookingSeatsRepo;
        this.requestRepo = requestRepo;
    }

    @Override
    public ReservationResponse rserveSeats(BookingRequestDto requestDto) {
        BookingRequest request=toRequest(requestDto);
        if (areSeatsAvailable(request)) {
            // Reserve seats and confirm the booking
            reserveSeatsAndUpdateDatabase(request);
            return new ReservationResponse(true, "Booking confirmed");
        } else {
            // Provide alternative options or notify the user of unavailability
            return new ReservationResponse(false, "Seats not available");
        }
    }




    @Transactional
    @Override
    public void cancelReservation(int bookingId) {
        Seat seat=this.seatRepo.findById(bookingId).orElseThrow(()->new ResourceNotFoundException("Seat","bookingId",bookingId));
//        Optional<BookingRequest> optionalBooking = requestRepo.findById(bookingId);
        BookingRequest booking = seat.getBooking();
        if (booking!=null) {
           seat.setBooking(null);
           seat.setReserved(false);
            seatRepo.save(seat);

            // Save the updated booking entity
            requestRepo.delete(booking);
            // Perform cancellation logic
//            cancelSeats(booking.getSeats());
//            List<Seat> reservedSeats = booking.getSeats();
//            System.out.println("seat"+reservedSeats.toString());
//            reservedSeats.forEach(seat -> {
//                seat.setReserved(false);
//                seat.setBooking(null); // You may need to disassociate the seat from the booking
//            });

        } else {
            // Handle the case where the booking with the given ID is not found
            throw new BookingNotFoundException("Booking not found for ID: " + bookingId);
        }
    }
//    private void cancelSeats(List<Seat> seats) {
//        // Implement the logic to mark seats as not reserved
//        seats.forEach(seat -> seat.setReserved(false));
//        seatRepo.saveAll(seats);
//    }

    private boolean areSeatsAvailable(BookingRequest request) {
        long count1=seatRepo.count();
        long count = request.getNoOfSeats();
        return count <= count1;
    }

    private void reserveSeatsAndUpdateDatabase(BookingRequest request) {

            if(areSeatsAvailable(request)){
                int count= request.getNoOfSeats();
                List<Seat> reservedSeats=seatRepo.findFirstNByReservedFalse();
                if (reservedSeats.size() >= count) {
                    // Check if all the seats are not already reserved
                    boolean allSeatsNotReserved = reservedSeats.stream().noneMatch(Seat::isReserved);

                    if (allSeatsNotReserved) {
                        reservedSeats = reservedSeats.subList(0, count);
                        reservedSeats.forEach(seat -> seat.setReserved(true));
                        seatRepo.saveAll(reservedSeats);
                        BookingRequest booking = new BookingRequest();
//                        booking.setSeats(reservedSeats);
                        requestRepo.save(booking);
                    }
                    else{
                        throw new SeatsNotAvailableException("Some of the requested seats are already reserved");
                    }
                }
                else {

                    throw new SeatsNotAvailableException("Requested seats are not available");
                }

            }
            else {
                throw new SeatsNotAvailableException("Requested seats are not available.");
            }
        }

    public BookingRequest toRequest(BookingRequestDto bookingDto){
        return modelMapper.map(bookingDto,BookingRequest.class);
    }
}
