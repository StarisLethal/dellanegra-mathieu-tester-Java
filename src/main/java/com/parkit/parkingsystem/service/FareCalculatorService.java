package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket, Boolean discount){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        long inHour = ticket.getInTime().getTime() ;
        long outHour = ticket.getOutTime().getTime();


        //TODO: Some tests are failing here. Need to check if this logic is correct
        double duration = (outHour - inHour) / 3600000.00;

        switch (ticket.getParkingSpot().getParkingType()){
            case CAR: {

                if (discount){
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR_DISCOUNT);
                    System.out.println("discount applied");
                }
                else {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                }
                    if( duration <= 0.5){
                        ticket.setPrice(0);
                }
                break;
            }
            case BIKE: {

                if (discount){
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR_DISCOUNT);
                    System.out.println("Discount applied");
                }
                else {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                }
                if( duration <= 0.5){
                    ticket.setPrice(0);
                }
                break;
            }
            default: throw new IllegalArgumentException("Unkown Parking Type");
        }
    }

    public void calculateFare(Ticket ticket){

        calculateFare(ticket, false);

    }
}