package com.cumulocity.microservice.customdecoders.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataFragmentUpdate implements Serializable {
    private String key;
    private String value;
}
