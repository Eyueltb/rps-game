package com.rps.controllers;

import com.rps.dto.GameDTO;
import com.rps.exceptions.UseException;
import com.rps.exceptions.UseExceptionType;
import com.rps.models.Game;
import com.rps.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.rps.exceptions.UseExceptionType.GAME_NOT_FOUND;

@RequestMapping("/games")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders =  "*")

public class GameController {
    GameService gameService;

    @GetMapping("/start")
    public GameDTO startGame(@RequestHeader (value="token", required = false) String tokenId) throws UseException{
        return gameService.createGame(tokenId)
                .map(this::toGameDTO)
                .orElseThrow(()->new UseException(GAME_NOT_FOUND));
    }
    @GetMapping("/getGameId")
    public String getGameId( @RequestHeader(value="token", required = false) String tokenId)  throws UseException {
        return gameService.getGameId(tokenId);
    }
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
