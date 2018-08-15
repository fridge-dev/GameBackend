package com.frjgames.dal.ddb.typeconverters.types;

import lombok.Builder;
import lombok.Data;

/**
 * DynamoDB composite type of a world + level combination.
 *
 * @author fridge
 */
@Data
@Builder
public class WorldLevelDdbType {

    private final int worldNumber;

    private final int levelNumber;
}
