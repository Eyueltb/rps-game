package com.rps.controllers;

import com.rps.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/games")
@AllArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders =  "*")

public class GameController {
    GameService gameService;
}
