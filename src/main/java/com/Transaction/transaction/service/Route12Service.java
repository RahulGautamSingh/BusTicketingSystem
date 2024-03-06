package com.Transaction.transaction.service;


import com.Transaction.transaction.entity.BusStop;
import com.Transaction.transaction.entity.Route12;
import com.Transaction.transaction.payloads.BusStopDto;
import com.Transaction.transaction.payloads.Route12Dto;

import java.util.List;

public interface Route12Service {
    Route12Dto updateRoute(Route12Dto route12Dto,int id);
    void deleteRoute(int id);
    Route12Dto getRouteById(int id);
    List<Route12Dto> getAllRoute();
    Route12Dto createRouteWithBusStop(Route12Dto route12Dto,int id,int id1);
//    List<Route12Dto> findShortestRoute(int sourceId, int destinationId);

}
