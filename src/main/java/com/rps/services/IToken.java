package com.rps.services;

import com.rps.models.Token;

import java.util.Optional;

public interface IToken {
    Optional<Token> getAToken(String tokenId);
    boolean verifyToken(String tokenId);
}
