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


}
