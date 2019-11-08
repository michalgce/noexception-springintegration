package com.boldare.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class MetricDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String type;
    private final String unit;
    private final Double value;

    @Override
    public String toString() {
        return "MetricDTO{" +
                "type='" + type + '\'' +
                ", unit='" + unit + '\'' +
                ", value=" + value +
                '}';
    }
}
