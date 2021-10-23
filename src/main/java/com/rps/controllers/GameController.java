package com.rps.controllers;

import com.rps.dto.GameDTO;
import com.rps.enums.Move;
import com.rps.exceptions.UseException;
import com.rps.models.Game;
import com.rps.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.rps.exceptions.UseExceptionType.GAME_ALREADY_STARTED;
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
    @GetMapping("/join/{gameId}")
    public GameDTO joinGame(@PathVariable String gameId,
                         @RequestHeader (value="token", required = false) String tokenId) throws UseException {
        return gameService.joinGame(tokenId, gameId).map(this::toGameDTO).orElseThrow(()->new UseException(GAME_ALREADY_STARTED));
    }
    @GetMapping("/move/{sign}")
    public GameDTO makeMove(@RequestHeader(value = "token") String tokenId,
                         @PathVariable Move sign) throws UseException {
        return gameService.makeMove(sign, tokenId).map(this::toGameDTO).orElseThrow(()->new UseException(GAME_ALREADY_STARTED));
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
