package com.frjgames.app.api.models.interfaces;

import com.frjgames.app.api.models.exceptions.GameAlreadyMatchedException;
import com.frjgames.app.api.models.exceptions.IllegalGameUpdateException;
import com.frjgames.app.api.models.exceptions.InvalidInputException;
import com.frjgames.app.api.models.inputs.MatchGameInput;

/**
 * The top level handler for matching a game with a 2nd player.
 *
 * @author fridge
 */
public interface MatchGameHandler {

    void matchGame(MatchGameInput matchGameInput) throws InvalidInputException, IllegalGameUpdateException, GameAlreadyMatchedException;

}
