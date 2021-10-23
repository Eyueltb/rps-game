package com.rps.services;

import com.rps.models.Game;

import java.util.Optional;

public interface IGame {
    Optional<Game> createGame(String tokenId);
}
