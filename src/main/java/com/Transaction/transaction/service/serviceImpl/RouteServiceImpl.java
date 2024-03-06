package com.Transaction.transaction.service.serviceImpl;



import com.Transaction.transaction.entity.BusStop;
import com.Transaction.transaction.entity.Route12;
import com.Transaction.transaction.exception.ResourceNotFoundException;
import com.Transaction.transaction.model.Graph;
import com.Transaction.transaction.payloads.BusStopDto;
import com.Transaction.transaction.payloads.Route12Dto;
import com.Transaction.transaction.repository.BusStopRepo;
import com.Transaction.transaction.repository.RouteRepo;
import com.Transaction.transaction.service.Route12Service;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class  RouteServiceImpl implements Route12Service {
    private final RouteRepo routeRepo;
    private final ModelMapper modelMapper;
   private final BusStopRepo busStopRepo;
    private static final Logger logger = LoggerFactory.getLogger(RouteServiceImpl.class);
    private static final int INFINITY = Integer.MAX_VALUE;

    public RouteServiceImpl(RouteRepo routeRepo, ModelMapper modelMapper, BusStopRepo busStopRepo) {
        this.routeRepo = routeRepo;
        this.modelMapper = modelMapper;
        this.busStopRepo = busStopRepo;

    }

    @Override
    public Route12Dto updateRoute(Route12Dto route12Dto, int id) {
        Route12 route12=this.routeRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Route12","id",id));
        route12.setDate(route12Dto.getDate());
        Route12  route121=this.routeRepo.save(route12);
        return routeToDto(route121);
    }

    @Override
    public void deleteRoute(int id) {
        Route12 route12=this.routeRepo.findById(id).orElseThrow(()->new ResourceNotFoundException("Route12","id",id));
        BusStop busStop=route12.getDestinationBusStop();
        if(busStop!=null){
            busStop.getDestinationRoutes().remove(route12);
            busStopRepo.save(busStop);
        }
        BusStop busStop1=route12.getSourceBusStop();
        if(busStop1!=null){
            busStop1.getSourceRoutes().remove(route12);
            busStopRepo.save(busStop1);
        }
        this.routeRepo.delete(route12);
    }

    @Override
    public Route12Dto getRouteById(int id) {
        Route12 route12=this.routeRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Route12","id",id));
        return routeToDto(route12);
    }

    @Override
    public List<Route12Dto> getAllRoute() {
        List<Route12> route12s=this.routeRepo.findAll();
        return route12s.stream().map(this::routeToDto).collect(Collectors.toList());
    }

    @Override
    public Route12Dto createRouteWithBusStop(Route12Dto route12Dto, int id, int id1) {
        BusStop busStop = this.busStopRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("BusStop", "id", id));
        BusStop busStop1 = this.busStopRepo.findById(id1).orElseThrow(() -> new ResourceNotFoundException("BusStop", "id1", id1));
        Route12 route12 = this.dtoToRoute(route12Dto);
        route12.setSourceBusStop(busStop);
        route12.setDestinationBusStop(busStop1);
        Route12 route121 = this.routeRepo.save(route12);
        return routeToDto(route121);
    }

//    @Override
//    public List<Route12Dto> findShortestRoute(int sourceId, int destinationId) {
//        BusStop sourceBusStop = this.busStopRepo.findById(sourceId)
//                .orElseThrow(() -> new ResourceNotFoundException("BusStop", "id", sourceId));
//
//        BusStop destinationBusStop = this.busStopRepo.findById(destinationId)
//                .orElseThrow(() -> new ResourceNotFoundException("BusStop", "id1", destinationId));
//
//        List<BusStop> busStops = this.busStopRepo.findAll();
//        List<Route12> route12s = this.routeRepo.findAll();
//
//        Graph graph = new Graph();
//        graph.setRoute12s(route12s);
//        graph.setBusStops(busStops);
//
//        try {
//            initializeDistances(graph);
//            algorithmShortestPath.dijkstra(graph, sourceBusStop);
//        } catch (Exception e) {
//            // Handle the case where there is no path between source and destination
//            return Collections.emptyList();
//        }
//
//        List<Route12> shortestPath = retrieveShortestPath(sourceBusStop, destinationBusStop);
//        return shortestPath.stream().map(this::routeToDto).collect(Collectors.toList());
//    }
//
//    private List<Route12> retrieveShortestPath(BusStop sourceBusStop, BusStop destinationBusStop) {
//        List<Route12> shortestPath = new ArrayList<>();
//        BusStop currentBusStop = destinationBusStop;
//
//        while (currentBusStop != null && currentBusStop != sourceBusStop) {
//            List<Route12> routesToCurrentBusStop = getRoutesToBusStop(currentBusStop);
//
//            if (!routesToCurrentBusStop.isEmpty()) {
//                Route12 route = routesToCurrentBusStop.get(0);
//                shortestPath.add(route);
//                currentBusStop = route.getSourceBusStop();
//            } else {
//                break;
//            }
//        }
//
//        // Add the source bus stop if a path is found
//        if (currentBusStop == sourceBusStop) {
//            Collections.reverse(shortestPath);
//        } else {
//            shortestPath.clear();  // No path found, clear the list
//        }
//
//        return shortestPath;
//    }
//
//
//    private List<Route12> getRoutesToBusStop(BusStop currentBusStop) {
//        return currentBusStop.getDestinationRoutes();
//    }
//    private void initializeDistances(Graph graph) {
//        for (BusStop busStop : graph.getBusStops()) {
//            busStop.setDistance(INFINITY);
//        }
//    }


    public Route12 dtoToRoute (Route12Dto route12Dto){
            return this.modelMapper.map(route12Dto, Route12.class);
        }
        public Route12Dto routeToDto (Route12 route12){
            return this.modelMapper.map(route12, Route12Dto.class);
        }

    public List<Route12Dto> routeToDto1 (List<Route12> route12){
        return Collections.singletonList(this.modelMapper.map(route12, Route12Dto.class));
    }
    public BusStop dtoToBusStop(BusStopDto busStopDto){
        return this.modelMapper.map(busStopDto, BusStop.class);
    }

}
