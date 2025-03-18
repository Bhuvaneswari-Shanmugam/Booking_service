package com.bus.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginationDTO {

    private int page = 0;
    private int size = 10;
    private String sortBy = "id";
    private String direction = "ASC";
}
