package com.rps.controllers;

import com.rps.dto.TokenDTO;
import com.rps.exceptions.UseException;
import com.rps.exceptions.UseExceptionType;
import com.rps.models.CreateName;
import com.rps.models.Token;
import com.rps.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor

@CrossOrigin(origins = "*", allowedHeaders = "*")

public class TokenController {
    TokenService tokenService;

    @GetMapping("/token")
    public String createToken() {
        return tokenService.createToken().getId();
    }
    @PostMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Token> setName(@RequestBody CreateName createName,
                                         @RequestHeader (value="token", required = false) String tokenId) throws UseException {
        Token token=tokenService.getTokenById(tokenId).orElseThrow(()->new UseException(UseExceptionType.TOKEN_NOT_FOUND));

        if(token!=null)
            token.setName(createName.getName());
        tokenService.saveToken(token);
       //return new ResponseEntity(toTokenDTO(token), HttpStatus.CREATED);
       return null;
    }
    private TokenDTO toTokenDTO(Token token){
        return new TokenDTO(
                token.getId(),
                token.getOwnedGame().getId(),
                token.getName(),
                (token.getJoinGame()!=null ) ? token.getJoinGame().getId():" "
        );
    }
}
