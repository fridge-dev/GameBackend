package com.frjgames.dal.ddb.typeconverters.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;

/**
 * Converter for the {@link WorldLevelDdbType} POJO.
 *
 * @author fridge
 */
public class WorldLevelDdbTypeConverter implements DynamoDBTypeConverter<String, WorldLevelDdbType> {

    private static final String SEPARATOR = "::";

    @Override
    public String convert(final WorldLevelDdbType worldLevelDdbType) {
        return worldLevelDdbType.getWorldNumber() + SEPARATOR + worldLevelDdbType.getLevelNumber();
    }

    @Override
    public WorldLevelDdbType unconvert(final String string) {
        String[] split = string.split(SEPARATOR);
        int worldNumber = Integer.parseInt(split[0]);
        int levelNumber = Integer.parseInt(split[1]);

        return WorldLevelDdbType.builder()
                .worldNumber(worldNumber)
                .levelNumber(levelNumber)
                .build();
    }

}
