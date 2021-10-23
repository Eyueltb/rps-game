package com.rps.services;

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
    @Override
    public Optional<Game> joinGame(String tokenId, String ownerGameId) throws UseException {
        Token opponentToken = getToken(tokenId).get();
        Game gameEntity = gameRepository.findById(ownerGameId).orElseThrow(()->new UseException(GAME_NOT_FOUND));
        switch (gameEntity.getGameStatus()){
            case WIN,LOSE, DRAW -> throw new UseException(GAME_NOT_FOUND);
            case ACTIVE-> throw new UseException(GAME_ALREADY_STARTED);
            case OPEN->  gameEntity = getAndSetStatusWhenJoinGame(opponentToken, gameEntity);
            default -> throw new IllegalStateException("Unexpected value: " + gameEntity.getGameStatus());
        };
        return Optional.of(gameEntity);
    }
    private Game getAndSetStatusWhenJoinGame(Token opponentToken, Game gameEntity) {
        gameEntity.setOpponent(opponentToken);
        gameEntity.setGameStatus(Status.ACTIVE);
        opponentToken.setJoinGame(gameEntity);
        opponentToken.setName(opponentToken.getName());
        tokenRepository.save(opponentToken);
        //gameEntity =gameRepository.save(gameEntity);
        return gameRepository.save(gameEntity);
    }

    private Optional<Token> getToken(String tokenId) throws UseException {
        if(tokenRepository.findAll().stream().noneMatch(t->t.getId().equals(tokenId)))
            throw new UseException(UseExceptionType.TOKEN_NOT_FOUND);
        return tokenRepository.findAll().stream().filter(t->t.getId().equals(tokenId)).findAny();
    }


    private Stream<Game> getGameByGameStatus(Status gameStatus) {
        return switch(gameStatus){
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
