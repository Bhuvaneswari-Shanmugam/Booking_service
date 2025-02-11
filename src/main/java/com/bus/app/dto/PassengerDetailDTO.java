package com.bus.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PassengerDetailDTO {

    private String firstName;
    private String lastName;
    private Long age;
    private String gender;
    private String userId;

}
