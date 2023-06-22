package com.example.aoo.dao.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorCommandResponse {
    private String triedCommand;
    private List<Enum> l ;
    private String message ;
}