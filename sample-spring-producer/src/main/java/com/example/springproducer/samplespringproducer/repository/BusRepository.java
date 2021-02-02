package com.example.springproducer.samplespringproducer.repository;

import com.example.springproducer.samplespringproducer.model.BusData;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.util.List;

public class BusRepository {
    private final JdbcTemplate jdbcTemplate;

    public BusRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<BusData> selectAll() {
        return jdbcTemplate.query("select * from D_BUSMVT", busRowMapper());
    }

    private RowMapper<BusData> busRowMapper() {
        return (result, rowNum) -> {
            BusData data = new BusData();
            data.setVeh_id(result.getInt("veh_id"));
            data.setVeh_no(result.getString("veh_no"));
            data.setMapng_utmx(result.getDouble("mapng_utmx"));
            data.setMapng_utmy(result.getDouble("mapng_utmy"));
            data.setFSTTM(result.getString("FSTTM"));
            return data;
        };
    }
}
