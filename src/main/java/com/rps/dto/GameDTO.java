package com.rps.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

@Value
public class GameDTO {
    String id;
    String ownerTokenId;
    String ownerName;
    String opponentTokenId;
    String opponentName;
    String gameStatus;
    String ownerMove;
    String opponentMove;
    @JsonCreator
    public GameDTO(
            @JsonProperty("id") String id,
            @JsonProperty("ownerTokenId") String ownerTokenId,
            @JsonProperty("ownerName")   String ownerName,
            @JsonProperty("opponentTokenId") String opponentTokenId,
            @JsonProperty("opponentName") String opponentName,
            @JsonProperty("gameStatus") String gameStatus,
            @JsonProperty("ownerMove")   String ownerMove,
            @JsonProperty("opponentMove") String opponentMove) {
        this.id=id;
        this.ownerTokenId=ownerTokenId;
        this.ownerName=ownerName;
        this.opponentTokenId=opponentTokenId;
        this.opponentName=opponentName;
        this.gameStatus=gameStatus;
        this.ownerMove=ownerMove;
        this.opponentMove=opponentMove;
    }
}
