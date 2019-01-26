package com.frjgames.dal.models.enums;

/**
 * TODO
 *
 * @author TODO
 */
public enum GameStatusResultType {

    /**
     * Waiting for 2nd player to join
     */
    UNMATCHED,

    /**
     * Host deleted the game (before it was matched)
     */
    DELETED,

    /**
     * Game is in progress
     */
    IN_PROGRESS,

    /**
     * Match played until victory
     */
    COMPLETED_VICTORY,

    /**
     * Match played until cats game
     */
    COMPLETED_DRAW,

    /**
     * Player forfeited
     */
    COMPLETED_FORFEIT,

}
