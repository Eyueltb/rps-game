package com.rps.services;

import com.rps.exceptions.UseException;
import com.rps.exceptions.UseExceptionType;
import com.rps.models.Token;
import com.rps.repositories.TokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor

public class TokenService implements IToken {
    TokenRepository tokenRepository;
    @Override
    public Token createToken() {
        return tokenRepository.save(Token.create());
    }

    @Override
    public boolean verifyToken(String tokenId) {
     return tokenRepository.findAll().stream().anyMatch(token -> token.getId().equals(tokenId));
    }
    @Override
    public Optional<Token> getAToken(String tokenId) throws UseException {
        return Optional.ofNullable(tokenRepository.findById(tokenId).orElseThrow(() -> new UseException(UseExceptionType.TOKEN_NOT_FOUND)));
    }

    public Optional<Token> getTokenById(String tokenId) {
            return tokenRepository.findById(tokenId);
    }

    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }
}
