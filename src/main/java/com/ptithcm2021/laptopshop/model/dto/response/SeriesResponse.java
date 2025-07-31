package com.ptithcm2021.laptopshop.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeriesResponse {
    private int id;
    private String name;
    private String description;
    private String date;
    private String brandName;
}
