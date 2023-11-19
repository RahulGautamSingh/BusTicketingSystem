package com.Transaction.transaction.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Route")
public class Route12 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fro;
    private String too;
    private double distance;
    private int weight;
    private Date date;
    @OneToMany(mappedBy = "route12",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    Set<BusInfo> busInfos;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="source_id")
    private BusStop sourceBusStop;
    @ManyToOne
    @JoinColumn(name="destination_id")
    private BusStop destinationBusStop;

}
