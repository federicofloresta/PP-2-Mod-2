package com.techelevator.dao;

import com.techelevator.model.Park;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class JdbcParkDao implements ParkDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcParkDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Override
    public List<Park> getAllParks() {
        List<Park> allParks = new ArrayList<>();
        String parkSearch = "SELECT * FROM park ORDER BY location ASC;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(parkSearch);
        while (results.next()){
            allParks.add(mapRowToPark(results));
        }
        return allParks;
    }

    private Park mapRowToPark(SqlRowSet results) {
        Park park = new Park();
        park.setParkId(results.getInt("park_id"));
        park.setName(results.getString("name"));
        park.setLocation(results.getString("location"));
        park.setEstablishDate(Objects.requireNonNull(results.getDate("establish_date")).toLocalDate());
        park.setArea(results.getInt("area"));
        park.setVisitors(results.getInt("visitors"));
        park.setDescription(results.getString("description"));
        return park;
    }

}
