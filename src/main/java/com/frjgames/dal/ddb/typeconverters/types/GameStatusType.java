package com.frjgames.dal.ddb.typeconverters.types;

/**
 * Status of a game.
 *
 * @see com.frjgames.dal.ddb.items.GameDdbItem#status
 *
 * @author fridge
 */
public enum GameStatusType {

    /**
     * waiting for 2nd player to join
     */
    UNMATCHED,

    /**
     * game in progress
     */
    IN_PROGRESS,

    /**
     * match is completed
     */
    COMPLETE,

    /**
     * host deleted game
     */
    DELETED,

}
