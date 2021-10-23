package com.rps.services;

import com.rps.exceptions.UseException;
import com.rps.models.Token;

import java.util.Optional;

public interface IToken {
    Token createToken();
    boolean verifyToken(String tokenId);
    Optional<Token> getAToken(String tokenId) throws UseException;
}
