package com.rps.services;

import com.rps.exceptions.UseException;
import com.rps.models.Game;

import java.util.Optional;

public interface IGame {
    Optional<Game> createGame(String tokenId) throws UseException;
    Optional<Game> joinGame(String tokenId, String ownerId);
}
