package com.frjgames.dal.ddb.typeconverters.types;

/**
 * DynamoDBMapper representation of a player.
 *
 * Used in conjunction with {@link com.frjgames.dal.ddb.typeconverters.converters.GameBoardPlayerDdbTypeConverter}.
 *
 * @author fridge
 */
public enum GameBoardPlayerDdbType {
    PLAYER_ONE,
    PLAYER_TWO,
    NEITHER
}
