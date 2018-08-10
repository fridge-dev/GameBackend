package com.frjgames.app.api;

import com.frjgames.app.api.models.inputs.SaveEverlastHighScoreInput;
import com.frjgames.app.api.models.outputs.SaveEverlastHighScoreOutput;

/**
 * Top level API for saving high scores of a specific level in the Everlast game.
 *
 * @author fridge
 */
public interface SaveEverlastHighScore extends ApiHandler {

    SaveEverlastHighScoreOutput handle(SaveEverlastHighScoreInput input);

}
