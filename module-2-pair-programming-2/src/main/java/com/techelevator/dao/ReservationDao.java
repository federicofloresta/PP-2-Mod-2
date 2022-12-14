package com.techelevator.dao;

import com.techelevator.model.Reservation;

import java.time.LocalDate;
import java.util.List;

public interface ReservationDao {

    int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate);
    List<Reservation> upcomingReservations (int parkId);
    List<Reservation> availableSites (int parkId, int siteId);
}
