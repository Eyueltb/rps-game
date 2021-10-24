package com.rps;

import com.rps.controllers.GameController;
import com.rps.controllers.TokenController;
import com.rps.dto.GameDTO;
import com.rps.dto.TokenDTO;
import com.rps.enums.Move;
import com.rps.enums.Status;
import com.rps.exceptions.UseException;
import com.rps.models.CreateName;
import com.rps.models.Token;
import com.rps.repositories.GameRepository;
import com.rps.repositories.TokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
class RpsApplicationTests {
    @Autowired TokenController tokenController;
    @Autowired TokenRepository tokenRepository;
    @Autowired GameController gameController;
    @Autowired GameRepository gameRepository;


    @Test
    void test_get_new_token() throws UseException {
        //when
        TokenDTO token=tokenController.getTokenById(tokenController.createToken());
        //Then
        assertNotNull(token);
    }
    @Test
    void test_set_name() throws UseException {
        //Given
        String id= UUID.randomUUID().toString();
        CreateName name=new CreateName("Ermi");
        tokenController.setName(name,tokenController.createToken() );
        tokenRepository.save(new Token(id,null,name.getName(),null));
        //When
        TokenDTO token=tokenController.getTokenById(id);
        //Then
        assertEquals(id, token.getId());
        assertEquals(name.getName(), token.getName());
    }
    @Test
    void test_verify_token_exist() {
        //when
        String tokenId=  tokenController.createToken();
        assertTrue(tokenController.verifyToken(tokenId));
    }
    @Test
    void test_start_game() throws UseException {
        //Given
        String tokenId=tokenController.createToken();
        GameDTO game=gameController.startGame(tokenId);
        //When
        GameDTO owner=gameController.gameGameByTokenId(tokenId);
        // Then
        assertEquals(Status.OPEN.name(),  game.getGameStatus());
        assertEquals(game.getId(),  owner.getId());
        assertEquals(game.getOwnerTokenId(),  owner.getOwnerTokenId());
    }
    @Test
    void test_join_game() throws UseException {
        //Given

        String ownerTokenId=tokenController.createToken();
        String opponentTokenId=tokenController.createToken();
        GameDTO owner=gameController.startGame(ownerTokenId);
        //When
        GameDTO joiner=gameController.joinGame(owner.getId(),opponentTokenId);
        //Then
        assertEquals(Status.OPEN.name(), owner.getGameStatus());
        assertEquals(owner.getId(), joiner.getId());
        assertEquals(owner.getOwnerTokenId(), ownerTokenId);
        assertEquals(joiner.getOpponentTokenId(), opponentTokenId);
        assertEquals(Status.ACTIVE.name(), joiner.getGameStatus());
    }
    @Test
    void test_get_game_by_id() throws UseException {
        //Given
        String tokenId=tokenController.createToken();
        GameDTO game=gameController.startGame(tokenId);
        //When
        GameDTO owner=gameController.gameInfo(game.getId(),tokenId);
        //Then
        assertEquals(Status.OPEN.name(), owner.getGameStatus());
        assertEquals(owner.getId(), game.getId());
        assertEquals(game.getOwnerTokenId(), tokenId);
    }
    @Test
    void test_make_move() throws UseException {
        //Given
        String ownerTokenId=tokenController.createToken();
        String opponentTokenId=tokenController.createToken();
        GameDTO owner=gameController.startGame(ownerTokenId);
        GameDTO joiner=gameController.joinGame(owner.getId(),opponentTokenId);
        //when
        gameController.makeMove(ownerTokenId, Move.PAPER);
        gameController.makeMove(opponentTokenId, Move.PAPER);
        String status=gameController.gameGameByTokenId(opponentTokenId).getGameStatus();
        //Then
        assertEquals(status,Status.DRAW.name());
    }
}
