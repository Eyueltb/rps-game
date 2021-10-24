package com.rps.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class TokenDTO {
    String id;
    String ownedGameId;
    String name;
    String joinedGameId;

    @JsonCreator
    public TokenDTO(
            @JsonProperty("id") String id,
            @JsonProperty("ownedGameId") String ownedGameId,
            @JsonProperty("name")   String name,
            @JsonProperty("joinedGameId") String joinedGameId) {
        this.id=id;
        this.ownedGameId=ownedGameId;
        this.name=name;
        this.joinedGameId=joinedGameId;
    }
}
