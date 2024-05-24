package com.divum.hiring_platform.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PartWiseResponse {

    private String category;
    private List<UserResponse> userResponse;

    @Override
    public String toString() {
        return "PartWiseResponse{" +
                "category='" + category + '\'' +
                ", userResponse=" + userResponse +
                '}';
    }
}
