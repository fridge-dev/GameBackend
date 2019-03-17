package com.frjgames.app.api.handlers;

import com.frjgames.app.api.models.exceptions.DuplicateGameException;
import com.frjgames.app.api.models.exceptions.IllegalGameUpdateException;
import com.frjgames.app.api.models.exceptions.InvalidInputException;
import com.frjgames.app.api.models.inputs.CreateGameInput;
import com.frjgames.app.api.models.inputs.MatchGameInput;
import com.frjgames.app.api.models.interfaces.CreateGameHandler;
import com.frjgames.app.api.models.interfaces.MatchGameHandler;
import com.frjgames.dal.models.data.MatchMadeGame;
import com.frjgames.dal.models.exceptions.ConditionalWriteException;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.exceptions.MissingDataException;
import com.frjgames.dal.models.interfaces.MatchMadeGameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import lombok.RequiredArgsConstructor;

/**
 * Top level class for creating a game.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class GameHandlerImpl implements CreateGameHandler, MatchGameHandler {

    private final MatchMadeGameAccessor gameAccessor;

    @Override
    public void createGame(final CreateGameInput createGameInput) throws DuplicateGameException {
        MatchMadeGame matchMadeGame = MatchMadeGame.builder()
                .gameId(createGameInput.getGameId())
                .gameName(createGameInput.getGameName())
                .hostUserId(createGameInput.getOwnerUserId())
                .creationTimeMs(createGameInput.getCreationTimeMs())
                .isGameUnmatched(true) // unused
                .build();

        try {
            gameAccessor.create(matchMadeGame);
        } catch (ConditionalWriteException e) {
            throw new DuplicateGameException("Game already exists.", e);
        }
    }

    @Override
    public void matchGame(final MatchGameInput matchGameInput) throws InvalidInputException, IllegalGameUpdateException {
        GameIdKey key = GameIdKey.builder()
                .gameId(matchGameInput.getGameId())
                .build();

        // TODO in this handler, we should load the match, validate its data is okay to "match", then call accessor.update().
        // This would allow us to throw more specific exceptions. Currently the Accessor has all the biz logic. Whoops!
        try {
            gameAccessor.updateMatchWithGuestUser(key, matchGameInput.getPlayerId());
        } catch (MissingDataException e) {
            throw new InvalidInputException("Game doesn't exist.", e);
        } catch (InvalidDataException e) {
            throw new IllegalGameUpdateException("Can't update the game for some reason. (see the comment above)", e);
        }
    }
}
