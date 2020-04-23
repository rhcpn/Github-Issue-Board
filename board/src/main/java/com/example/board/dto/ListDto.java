package com.example.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ListDto {
    private Long number;
    private String title;
    private String state;
    private String user;
    private String createAt;

}