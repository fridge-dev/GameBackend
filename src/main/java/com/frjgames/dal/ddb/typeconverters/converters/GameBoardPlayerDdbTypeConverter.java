package com.frjgames.dal.ddb.typeconverters.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.frjgames.dal.ddb.typeconverters.types.GameBoardPlayerDdbType;
import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;

/**
 * Converter for the {@link GameBoardPlayerDdbType} enum.
 *
 * @author fridge
 */
public class GameBoardPlayerDdbTypeConverter implements DynamoDBTypeConverter<Integer, GameBoardPlayerDdbType> {

    private static final BiMap<GameBoardPlayerDdbType, Integer> MAP = ImmutableBiMap.of(
            GameBoardPlayerDdbType.PLAYER_ONE, 1,
            GameBoardPlayerDdbType.PLAYER_TWO, 2,
            GameBoardPlayerDdbType.NEITHER, -1
    );

    @Override
    public Integer convert(final GameBoardPlayerDdbType appType) {
        return checkNonNull(MAP.get(appType), "No DDB value mapping for Player type '%s'.", appType);
    }

    @Override
    public GameBoardPlayerDdbType unconvert(final Integer ddbType) {
        return checkNonNull(MAP.inverse().get(ddbType), "Invalid DDB value '%s' for player type enum.", ddbType);
    }

    private <T> T checkNonNull(final T obj, final String messageFormat, final Object... args) {
        if (obj == null) {
            throw new IllegalArgumentException(String.format(messageFormat, args));
        }

        return obj;
    }
}
