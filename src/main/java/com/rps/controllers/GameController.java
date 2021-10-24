package com.rps.controllers;

import com.rps.dto.GameDTO;
import com.rps.enums.Move;
import com.rps.enums.Status;
import com.rps.exceptions.UseException;
import com.rps.models.Game;
import com.rps.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.rps.exceptions.UseExceptionType.GAME_ALREADY_STARTED;
import static com.rps.exceptions.UseExceptionType.GAME_NOT_FOUND;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders =  "*")

public class GameController {
    GameService gameService;
    /** Todo:- To start the game by owner token, and make the game OPEN for other to join
     *  @param tokenId:- a particular a game player in all communication.
     */
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
    /** Todo:- To join the open games by other player */
    @GetMapping("/join/{gameId}")
    public GameDTO joinGame(@PathVariable String gameId,
                         @RequestHeader (value="token", required = false) String tokenId) throws UseException {
        return gameService.joinGame(tokenId, gameId).map(this::toGameDTO).orElseThrow(()->new UseException(GAME_ALREADY_STARTED));
    }
    /** Todo:- To play game (make move).
     *  @param sign:- a move by a given token (player)
     */
    @GetMapping("/move/{sign}")
    public GameDTO makeMove(@RequestHeader(value = "token") String tokenId,
                         @PathVariable Move sign) throws UseException {
        return gameService.makeMove(sign, tokenId).map(this::toGameDTO).orElseThrow(()->new UseException(GAME_ALREADY_STARTED));
    }
    /** Todo:- To Fetch the Game associated with a given token.*/
    @GetMapping("/getGameByTokenId")
    public GameDTO gameGameByTokenId(@RequestHeader (value="token", required = false) String tokenId) throws UseException {
        return gameService.getGameByTokenId(tokenId).map(this::toGameDTO).findAny().orElseThrow(()->new UseException(GAME_NOT_FOUND));
    }
    @GetMapping("/getGame/{gameId}")
    public GameDTO getGameById( @PathVariable String gameId){
        return gameService.getGameById(gameId).map(this::toGameDTO).get();
    }
    /** Todo:- Search the Game based on ( NONE, OPEN,ACTIVE, WIN, LOSE, DRAW )  */
    @GetMapping("/search/{status}")
    public List<GameDTO> searchByGameStatus(@PathVariable Status status)throws UseException {
        return gameService.filterByStatus(status).map(this::toGameDTO).collect(Collectors.toList());
    }
    /** Todo:- To find available open games */
    @GetMapping("")
    public List<GameDTO> getGamesList(@RequestHeader (value="token", required = false) String tokenId) throws UseException {
        return gameService.getGamesList(tokenId).map(this::toGameDTO).collect(Collectors.toList());
    }
    /** Todo:- To find available open games by token and gameId */
    @GetMapping("/{gameId}")
    public GameDTO gameInfo(
            @PathVariable String gameId,
            @RequestHeader (value="token", required = false) String tokenId) throws  UseException {
        return gameService.getGame(gameId,tokenId).map(this::toGameDTO).orElseThrow(()->new UseException(GAME_NOT_FOUND));
    }

    /** Todo:- map Entity Game to GameDTO */
    public GameDTO toGameDTO(Game game) {
        return new GameDTO(
                game.getId(),
                (game.getPlayer()!=null)? game.getPlayer().getId():"",
                (game.getPlayer()!=null)?  game.getPlayer().getName():"",
                (game.getOpponent()!=null)? game.getOpponent().getId():"",
                (game.getOpponent()!=null)? game.getOpponent().getName():"",
                game.getGameStatus().toString(),
                (game.getOwnerMove()!=null)?  game.getOwnerMove().toString(): "",
                (game.getOpponentMove()!=null)? game.getOpponentMove().toString() :""
        );
    }
}
