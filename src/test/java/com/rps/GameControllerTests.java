package com.rps;

import com.rps.controllers.GameController;
import com.rps.controllers.TokenController;
import com.rps.dto.GameDTO;
import com.rps.dto.TokenDTO;
import com.rps.exceptions.UseException;
import com.rps.models.Game;
import com.rps.models.Token;
import com.rps.repositories.GameRepository;
import com.rps.repositories.TokenRepository;
import com.rps.services.GameService;
import com.rps.services.TokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.reactive.function.client.WebClient;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integrationtest")
public class GameControllerTests {
    @MockBean TokenService tokenService;
    @MockBean GameService gameService;
    @LocalServerPort int port;

    @Autowired TokenRepository tokenRepository;
    @Autowired TokenController tokenController;
    @Autowired GameRepository gameRepository;
    @Autowired GameController gameController;
    TokenDTO tokenDTO1;
    TokenDTO tokenDTO2;
    Token token1;
    Token token2;
    GameDTO gameDTO1;
    GameDTO gameDTO2;
    Game game1;
    Game game2;
    @BeforeEach
    @Transactional
    void setUp() throws UseException {

        token1=tokenService.createToken();
        token2=tokenService.createToken();

        Game game1= gameService.createGame(token1.getId()).get();
        Game game2 = gameService.createGame(token2.getId()).get();

        token1=tokenRepository.save(token1);
        token2=tokenRepository.save(token2);

        game1=gameRepository.save(game1);
        game2=gameRepository.save(game2);

        tokenDTO1 = tokenController.toTokenDTO(token1);
        tokenDTO2 = tokenController.toTokenDTO(token2);
        gameDTO1 = gameController.toGameDTO(game1);
        gameDTO2 = gameController.toGameDTO(game2);
    }
    @AfterEach
    void tearDown() {
        gameRepository.deleteAll();
        tokenRepository.deleteAll();
    }
    @Test
    void test_createToken_success() {
        when(tokenService.getTokenById(token1.getId())).thenReturn(Optional.ofNullable(token1));
        List<TokenDTO> tokenDTOS = getToken(token1.getId());
        //Then
        assertEquals(List.of(tokenDTO1),tokenDTOS);
        assertEquals(1,tokenDTOS.size());

    }
    private List<GameDTO> getGame(String url) {
        WebClient webClient = WebClient.create("http://localhost:" + port + "/games" + url);
        return webClient.get()
                .retrieve()
                .bodyToFlux(GameDTO.class)
                .collectList()
                .block();
    }
    private List<TokenDTO> getToken(String url) {
        WebClient webClient = WebClient.create("http://localhost:" + port + "/auth" + url);
        return webClient.get()
                .retrieve()
                .bodyToFlux(TokenDTO.class)
                .collectList()
                .block();
    }
}
