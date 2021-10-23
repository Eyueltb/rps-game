package com.rps.repositories;

import com.rps.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository  extends JpaRepository<Game, String> {
}
