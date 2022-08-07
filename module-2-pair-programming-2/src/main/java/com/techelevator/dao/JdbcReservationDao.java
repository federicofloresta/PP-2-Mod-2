package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {
        Reservation reservation = new Reservation();
        String createResv = "INSERT INTO reservation (site_id, name, from_date, to_date) VALUES (?, ?, ?, ?) RETURNING reservation_id;";
        int reservationId = jdbcTemplate.queryForObject(createResv, int.class, siteId, name, fromDate, toDate);
        return reservationId;
    }

    @Override
    public List<Reservation> upcomingReservations (int parkId){
        List<Reservation> reservations = new ArrayList<>();
        String search = "SELECT * FROM reservation " +
                "JOIN site on reservation.site_id = site.site_id" +
                " JOIN campground on site.campground_id = campground.campground_id " +
                "WHERE campground.park_id = ? AND reservation.from_date >= CURRENT_TIMESTAMP AND reservation.to_date <= current_TIMESTAMP + interval '30 days'";
        SqlRowSet results = jdbcTemplate.queryForRowSet(search, parkId);
        while (results.next()){
            reservations.add(mapRowToReservation(results));
        }
        return reservations;
    }

    @Override
    public List<Reservation> availableSites (int parkId, int siteId){
        List<Reservation> reservations = new ArrayList<>();
        String search = "SELECT * FROM reservation " +
                "right JOIN site on reservation.site_id = site.site_id" +
                " JOIN campground on site.campground_id = campground.campground_id " +
                "WHERE campground.park_id = ? AND reservation.reservation_id is null AND site.site_id = ? returning site.site_id;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(search, parkId, siteId);
        while (results.next()){
            reservations.add(mapRowToReservation(results));
        }
        return reservations;
    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }


}
