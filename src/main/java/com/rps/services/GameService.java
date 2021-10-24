package com.rps.services;

import com.rps.enums.Move;
import com.rps.enums.Status;
import com.rps.exceptions.UseException;
import com.rps.exceptions.UseExceptionType;
import com.rps.models.CreateGame;
import com.rps.models.Game;
import com.rps.models.Token;
import com.rps.repositories.GameRepository;
import com.rps.repositories.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static com.rps.exceptions.UseExceptionType.GAME_ALREADY_STARTED;
import static com.rps.exceptions.UseExceptionType.GAME_NOT_FOUND;

@Service
@AllArgsConstructor
public class GameService implements IGame {
    GameRepository gameRepository;
    TokenRepository tokenRepository;
    /** Todo:- Create Game associated with token  */
    @Override
    public Optional<Game> createGame(String tokenId) throws UseException {
        Optional<Token> owner = getToken(tokenId);
        CreateGame createGame=new CreateGame(owner.get().getId(), owner.get().getName(), "","", Status.OPEN,"","");
        Game game= getGameEntity(owner, createGame);
        owner.get().setOwnedGame(game);
        tokenRepository.save(owner.get());
        game=gameRepository.save(game);
        return Optional.of(game);
    }

    public String getGameId(String tokenId) throws UseException {
        if(tokenRepository.findAll().stream().noneMatch(t->t.getId().equals(tokenId)))
            throw new UseException(UseExceptionType.TOKEN_NOT_FOUND);
        else if (tokenRepository.findAll().stream().filter(t->t.getId().equals(tokenId)).findAny().get().getOwnedGame().getId()==null)
            throw new UseException(UseExceptionType.TOKEN_NOT_FOUND);
        else
            return tokenRepository.findAll().stream().filter(t->t.getId().equals(tokenId)).findAny().get().getOwnedGame().getId();
    }
    private Game getGameEntity(Optional<Token> owner, CreateGame createGame) {
        String id= UUID.randomUUID().toString();
        Game gameEntity=new Game(
                id,
                owner.get(),
                null,
                createGame.getGameStatus(),
                null,
                null
        );
        return gameEntity;
    }
    /** Todo:- To join open games */
    @Override
    public Optional<Game> joinGame(String tokenId, String ownerGameId) throws UseException {
        Token opponentToken = getToken(tokenId).get();
        Game game = gameRepository.findById(ownerGameId).orElseThrow(()->new UseException(GAME_NOT_FOUND));
        switch (game.getGameStatus()){
            case WIN,LOSE, DRAW -> throw new UseException(GAME_NOT_FOUND);
            case ACTIVE-> throw new UseException(GAME_ALREADY_STARTED);
            case OPEN->  game = getAndSetStatusWhenJoinGame(opponentToken, game);
            default -> throw new IllegalStateException("Unexpected value: " + game.getGameStatus());
        };
        return Optional.of(game);
    }
    /** Todo:- to decide which player win based on a move provided by players( ROCK, PAPER, SCISSORS) */
    public Optional<Game> makeMove(Move sign, String tokenId) throws UseException {
        Token token = getToken(tokenId).get();
        tokenRepository.save(token);
        Optional<Game> getMove=null;
        if(token.getOwnedGame()!=null  &&  gameRepository.findAll().stream().anyMatch(g->g.getPlayer().getId().equals(tokenId)))
            getMove=getOwnerMove(sign, tokenId);
        else if(token.getJoinGame()!=null )
            getMove=getJoinerMOVE(sign, tokenId);
        return getMove;
    }
    /** Todo:- Fetch status of the game by TokenId */
    public Stream<Game> getGameByTokenId(String tokenId) throws UseException {
        Token token=getToken(tokenId).get();
        String gameId=getGameIdFromToken(token);
        return gameRepository.findAll().stream().filter(g->g.getId().equals(gameId));
    }
    /** Todo:- Filter game */
    public Stream<Game> filterByStatus(Status status) throws UseException {
        if(gameRepository.findAll().stream().findFirst().get()==null) throw new UseException(GAME_NOT_FOUND);
        return getGameByGameStatus(status);
    }
    public Optional<Game> getGame(String gameId, String tokenId) throws UseException {
        Token token=getToken(tokenId).get();
        if(token.getOwnedGame()==null)
            throw new UseException(GAME_NOT_FOUND);
        else
            return Optional.ofNullable(gameRepository.findById(token.getOwnedGame().getId())).get();
    }
    public Optional<Game> getGameById(String gameId) {
        return Optional.ofNullable(gameRepository.findById(gameId)).get();
    }
    /** Todo:- Fetch gameId from token */
    private String getGameIdFromToken(Token token) throws UseException  {
        if(tokenRepository.findAll().stream().anyMatch(t->t.getId().equals(token.getId()))){
            //if(token.getOwnedGame()!=null && token.getJoinGame()!=null){}
            if(token.getOwnedGame()!=null) return token.getOwnedGame().getId();
            else if(token.getJoinGame()!=null) return token.getJoinGame().getId();
            else
                throw new UseException(GAME_NOT_FOUND);
        }
        throw new UseException(UseExceptionType.TOKEN_NOT_FOUND);
    }
    /** Todo:- Fetch available open games */
    public Stream<Game> getGamesList(String tokenId) throws UseException{
        String gameId=getGameIdFromToken(getToken(tokenId).get());
        Stream<Game> games=filterByStatus(Status.OPEN);
        return games.filter(g->g.getId().equals(gameId));
    }

    private Optional<Game> getOwnerMove(Move sign, String tokenId) {
        Optional<Game> owner=gameRepository.findAll().stream().filter(g->g.getPlayer().getId().equals(tokenId)).findAny();
        owner.get().setOwnerMove(sign);
        gameRepository.save(owner.get());
        return Optional.of(updateGameStatus(owner));
    }
    private Optional<Game> getJoinerMOVE(Move sign, String tokenId) {
        Game joiner=tokenRepository.findById(tokenId).get().getJoinGame();
        //Optional<GameEntity> joiner=all().filter(g->g.getOpponent().getId().equals(tokenId)).findAny();
        joiner.setOpponentMove(sign);
        gameRepository.save(joiner);
        return Optional.of(updateGameStatus(Optional.of(joiner)));
    }
    private Game updateGameStatus(Optional<Game> gameEntity) {
        Move ownerMove = gameEntity.get().getOwnerMove();
        Move opponentMove = gameEntity.get().getOpponentMove();
        if(ownerMove!=null && opponentMove!=null){
            updateStatus(gameEntity, ownerMove, opponentMove);
        }
        return gameEntity.get();
    }
    private void updateStatus(Optional<Game> gameEntity, Move ownerMove, Move opponentMove) {
        switch (ownerMove) {
            case ROCK-> gameEntity.get().setGameStatus(
                    (opponentMove==Move.ROCK)? Status.DRAW: (opponentMove==Move.SCISSORS)? Status.WIN: Status.LOSE
            );
            case PAPER-> gameEntity.get().setGameStatus(
                    (opponentMove==Move.PAPER)? Status.DRAW: (opponentMove==Move.ROCK)? Status.WIN: Status.LOSE
            );
            case SCISSORS-> gameEntity.get().setGameStatus(
                    (opponentMove==Move.SCISSORS)? Status.DRAW: (opponentMove==Move.PAPER)? Status.WIN: Status.LOSE
            );
            default -> gameEntity.get().setGameStatus(Status.ACTIVE);
        }
        gameRepository.save(gameEntity.get());
    }
    private Game getAndSetStatusWhenJoinGame(Token opponentToken, Game game) {
        game.setOpponent(opponentToken);
        game.setGameStatus(Status.ACTIVE);
        opponentToken.setJoinGame(game);
        opponentToken.setName(opponentToken.getName());
        tokenRepository.save(opponentToken);
        //game =gameRepository.save(game);
        return gameRepository.save(game);
    }

    private Optional<Token> getToken(String tokenId) throws UseException {
        if(tokenRepository.findAll().stream().noneMatch(t->t.getId().equals(tokenId)))
            throw new UseException(UseExceptionType.TOKEN_NOT_FOUND);
        return tokenRepository.findAll().stream().filter(t->t.getId().equals(tokenId)).findAny();
    }

    private Stream<Game> getGameByGameStatus(Status status) {
        return switch(status){
            case OPEN-> filterGame(Status.OPEN);
            case ACTIVE-> filterGame(Status.ACTIVE);
            case WIN-> filterGame(Status.WIN);
            case LOSE-> filterGame(Status.LOSE);
            case DRAW-> filterGame(Status.DRAW);
        };
    }
    private Stream<Game> filterGame(Status status) {
        return gameRepository.findAll().stream().filter(g -> g.getGameStatus().equals(status));
    }
}
