package com.rps.controllers;

import com.rps.dto.TokenDTO;
import com.rps.exceptions.UseException;
import com.rps.models.CreateName;
import com.rps.models.Token;
import com.rps.services.TokenService;
import com.sun.istack.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.rps.exceptions.UseExceptionType.TOKEN_NOT_FOUND;

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
    @GetMapping("/{id}")
    public TokenDTO getTokenById(@PathVariable String id) throws UseException {
        return tokenService.getTokenById(id).map(this::toTokenDTO).orElseThrow(()->new UseException(TOKEN_NOT_FOUND));
    }
    @GetMapping("/verify/{token}")
    public boolean verifyToken(@PathVariable String token) {
        return tokenService.verifyToken(token);
    }

    @PostMapping("/name")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> setName(@RequestBody CreateName createName,
                                         @RequestHeader (value="token", required = false) String tokenId) throws UseException {
        Token token=tokenService.getTokenById(tokenId).orElseThrow(()->new UseException(TOKEN_NOT_FOUND));

        if(token!=null)
            token.setName(createName.getName());
        tokenService.saveToken(token);
        return new ResponseEntity<>(toTokenDTO(token), HttpStatus.CREATED);
        //return null;
    }

    public TokenDTO toTokenDTO(Token token){
        return new TokenDTO(
                token.getId(),
                (token.getOwnedGame()!=null) ? token.getOwnedGame().getId(): "",
                token.getName(),
                (token.getJoinGame()!=null ) ? token.getJoinGame().getId():""
        );
    }
}
