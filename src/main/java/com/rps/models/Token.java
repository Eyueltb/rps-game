package com.rps.models;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Data
@AllArgsConstructor()
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name="tokens")

public class Token {
    @Id
    @Column(columnDefinition = "varchar(100)") private String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="owned_game_id",referencedColumnName = "id")
    Game ownedGame;

    private  String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="join_game_id",referencedColumnName = "id")
    Game joinGame;


    public static Token create() {
        return new Token(UUID.randomUUID().toString(),null,"", null);
    }

}
