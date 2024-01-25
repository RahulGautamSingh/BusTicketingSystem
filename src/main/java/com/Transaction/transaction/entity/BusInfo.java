package com.Transaction.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="Bus")
public class BusInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String busName;
    private String busType;
    private double price;
    @ManyToOne
    @JoinColumn(name = "fid")
    private Route12 route12;
    @OneToMany(mappedBy = "busInfo",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    List<Seat> seats;
}
