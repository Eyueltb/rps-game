package com.rps.models;

import com.rps.enums.Status;
import lombok.Value;

@Value
public class CreateGame {

    String ownerTokenId;
    String ownerName;
    String opponentTokenId;
    String opponentName;
    Status gameStatus;
    String ownerMove;
    String opponentMove;

}
