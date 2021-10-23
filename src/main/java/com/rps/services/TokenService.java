package com.rps.services;

import com.rps.models.Token;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor

public class TokenService implements IToken{
    @Override
    public Optional<Token> getAToken(String tokenId) {
        return Optional.empty();
    }

    @Override
    public boolean verifyToken(String tokenId) {
        return false;
    }
}
