package com.rps.models;

import com.rps.enums.Move;
import com.rps.enums.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="games")

public class Game {
    @Id @Column(columnDefinition = "varchar(100)") String id;

    @OneToOne(mappedBy = "ownedGame")
    Token player;

    @OneToOne(mappedBy = "joinGame")
    Token opponent;

    @Enumerated(EnumType.STRING)
    Status gameStatus;
    @Enumerated(EnumType.STRING)
    Move ownerMove;
    @Enumerated(EnumType.STRING)
    Move opponentMove;

}
