package com.example.model;

import lombok.Data;

import java.util.List;

@Data
public class AvailableTagEvent {
    private String tag;
    private List<String> values;
}
