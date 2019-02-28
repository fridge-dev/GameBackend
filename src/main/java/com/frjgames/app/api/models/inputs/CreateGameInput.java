package com.frjgames.app.api.models.inputs;

import lombok.Builder;
import lombok.Data;

/**
 * Input for the {@link com.frjgames.app.api.models.interfaces.CreateGameHandler}.
 *
 * @author fridge
 */
@Data
@Builder
public class CreateGameInput {

    private final String gameId;

    private final String gameName;

    private final String ownerUserId;

    private final long creationTimeMs;

}
