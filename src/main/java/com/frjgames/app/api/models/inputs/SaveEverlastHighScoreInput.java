package com.frjgames.app.api.models.inputs;

import com.frjgames.app.api.models.interfaces.SaveEverlastHighScore;
import lombok.Builder;
import lombok.Data;

/**
 * Input for the {@link SaveEverlastHighScore} API.
 *
 * @author fridge
 */
@Data
@Builder
public class SaveEverlastHighScoreInput {

    /**
     * The display name of the user who achieved the score.
     */
    private final String username;

    /**
     * The number of the world.
     */
    private final int worldNumber;

    /**
     * The number of the level, unique within the world.
     */
    private final int levelNumber;

    /**
     * The numeric score of the level execution.
     */
    private final int score;

    /**
     * A blob of unformatted data which represents the level execution's ghost.
     */
    private final String ghostDataBlob;

    /**
     * The version number of the data blob's format/schema.
     */
    private final int ghostSchemaVersion;

}
