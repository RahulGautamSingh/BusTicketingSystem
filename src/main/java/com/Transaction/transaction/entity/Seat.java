package com.Transaction.transaction.entity;

import com.Transaction.transaction.model.SeatType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Seat1111")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String seatName;
    private boolean reserved;
    private String busName;
    @Enumerated(EnumType.STRING)
    private SeatType seatType;
    private double price;
    private String seatNumber;
    private String zone;



    public Seat(String seatNumber, String zone) {
        this.seatNumber = seatNumber;
        this.zone = zone;
    }

    public void cancelRequest(){
        this.reserved=false;
    }

    @ManyToOne
    @JoinColumn(name = "fid",referencedColumnName = "id")
    private BusInfo busInfo;
    @OneToOne(mappedBy = "seat",fetch = FetchType.EAGER)
    private Ticket ticket;
    @ManyToMany(mappedBy = "seats")
    private List<Booking> bookings;
    @ManyToOne
    @JoinColumn(name="id1")
    private BookingRequest booking;

    public Seat(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
