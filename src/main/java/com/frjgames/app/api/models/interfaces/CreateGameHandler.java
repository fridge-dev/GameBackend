package com.frjgames.app.api.models.interfaces;

import com.frjgames.app.api.models.exceptions.DuplicateGameException;
import com.frjgames.app.api.models.inputs.CreateGameInput;

/**
 * The handler for creating a game.
 *
 * @author fridge
 */
public interface CreateGameHandler extends ApiHandler {

    void createGame(CreateGameInput createGameInput) throws DuplicateGameException;

}
