package com.rps.services;

import com.rps.models.Game;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class GameService implements IGame{
    @Override
    public Optional<Game> createGame(String tokenId) {
        return Optional.empty();
    }

    @Override
    public Optional<Game> joinGame(String tokenId, String ownerId) {
        return Optional.empty();
    }
}
