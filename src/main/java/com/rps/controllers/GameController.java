package com.rps.controllers;

import com.rps.dto.GameDTO;
import com.rps.models.Game;
import com.rps.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/games")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders =  "*")

public class GameController {
    GameService gameService;


    private GameDTO toGameDTO(Game game) {
        return new GameDTO(
                game.getId(),
                game.getPlayer().getId(),
                game.getPlayer().getName(),
                game.getOpponent().getId(),
                game.getOpponent().getName(),
                game.getGameStatus().toString(),
                game.getOwnerMove().toString(),
                game.getOpponentMove().toString()
        );
    }
}
