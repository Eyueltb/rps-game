package com.rps.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class CreateName {
    String name;
    /** Cannot construct instance and cannot deserialize from Object value problem is fixed using @JsonCreator */
    @JsonCreator
    public CreateName(@JsonProperty("name")  String name) {
        super();
        this.name = name;
    }
}
