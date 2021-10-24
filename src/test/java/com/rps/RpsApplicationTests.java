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



}
