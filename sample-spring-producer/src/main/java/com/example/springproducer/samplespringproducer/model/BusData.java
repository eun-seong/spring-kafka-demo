package com.example.springproducer.samplespringproducer.model;

import lombok.Data;

@Data
public class BusData {
    private int veh_id;
    private String veh_no;
    private double mapng_utmx;
    private double mapng_utmy;
    private String FSTTM;
}
