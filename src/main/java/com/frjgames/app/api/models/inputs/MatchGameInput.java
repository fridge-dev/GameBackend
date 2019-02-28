package com.frjgames.app.api.models.inputs;

import lombok.Builder;
import lombok.Data;

/**
 * Input for the {@link com.frjgames.app.api.models.interfaces.MatchGameHandler}
 *
 * @author fridge
 */
@Data
@Builder
public class MatchGameInput {

    private final String gameId;

    private final String playerId;
}
