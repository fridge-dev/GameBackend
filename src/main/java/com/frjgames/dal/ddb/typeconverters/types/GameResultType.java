package com.frjgames.dal.ddb.typeconverters.types;

/**
 * Possible outcome/result of a game.
 *
 * @see com.frjgames.dal.ddb.items.GameDdbItem#result
 *
 * @author fridge
 */
public enum GameResultType {

    /**
     * Match played until victory
     */
    VICTORY,

    /**
     * Match played until cats game
     */
    DRAW,

    /**
     * Player forfeited
     */
    ABANDONED,

}
