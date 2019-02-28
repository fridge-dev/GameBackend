package com.frjgames.dal.accessors;

import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.GameDdbItem;
import com.frjgames.dal.ddb.typeconverters.types.GameResultType;
import com.frjgames.dal.ddb.typeconverters.types.GameStatusType;
import com.frjgames.dal.ddb.utils.DdbExceptionTranslator;
import com.frjgames.dal.models.data.Game;
import com.frjgames.dal.models.enums.GameStatusResultType;
import com.frjgames.dal.models.exceptions.InvalidDataException;
import com.frjgames.dal.models.exceptions.MissingDataException;
import com.frjgames.dal.models.interfaces.GameAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import com.frjgames.utils.FrjConditions;
import com.google.common.base.Preconditions;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.RequiredArgsConstructor;

/**
 * This class is responsible for accessing a {@link Game} from the DB.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class GameAccessorImpl implements GameAccessor {

    private final DynamoDbAccessor<GameDdbItem> ddbAccessor;

    /**
     * Not supported.
     * @see com.frjgames.dal.models.interfaces.MatchMadeGameAccessor
     */
    @Override
    public void create(final Game data) {
        throw new UnsupportedOperationException("Games are created by MatchMadeGameAccessor#updateMatchWithGuestUser()");
    }

    /**
     * Load a game.
     */
    @Override
    public Optional<Game> load(final GameIdKey key) {
        return ddbAccessor.loadItem(key.getGameId())
                .map(this::itemToDomainType);
    }

    private Game itemToDomainType(final GameDdbItem item) {
        return Game.builder()
                .gameId(item.getGameId())
                .hostUserId(item.getFirstUserId())
                .guestUserId(item.getSecondUserId())
                .status(makeStatusResult(item.getStatus(), item.getResult()))
                .winnerUserId(item.getWinnerUserId())
                .build();
    }

    private GameStatusResultType makeStatusResult(final GameStatusType status, final GameResultType result) {
        switch (status) {
            case UNMATCHED:
                return GameStatusResultType.UNMATCHED;

            case IN_PROGRESS:
                return GameStatusResultType.IN_PROGRESS;

            case DELETED:
                return GameStatusResultType.DELETED;

            case COMPLETE:
                switch (result) {
                    case DRAW:
                        return GameStatusResultType.COMPLETED_DRAW;

                    case VICTORY:
                        return GameStatusResultType.COMPLETED_VICTORY;

                    case ABANDONED:
                        return GameStatusResultType.COMPLETED_FORFEIT;

                    default:
                        throw new InvalidDataException(String.format("Unrecognized Result %s", result));
                }

            default:
                throw new InvalidDataException(String.format("Unrecognized status %s", status));
        }
    }

    /**
     * Update match with a result.
     */
    @Override
    public void updateGameCompleted(final GameIdKey key, final GameStatusResultType statusType, @Nullable final String winnerUserId) {
        // Load
        GameDdbItem gameItem = ddbAccessor.loadItem(key.getGameId())
                .orElseThrow(() -> new MissingDataException("Game doesn't exist for ID " + key.getGameId()));

        // Validate
        Preconditions.checkArgument(
                gameItem.getFirstUserId().equals(winnerUserId) || gameItem.getSecondUserId().equals(winnerUserId) || winnerUserId == null,
                "Attempted winner is not one of the 2 players."
        );
        FrjConditions.checkArg(
                gameItem.getStatus() == GameStatusType.IN_PROGRESS,
                () -> new InvalidDataException("Game must be in state in-progress, but is in " + gameItem.getStatus())
        );
        FrjConditions.checkArg(
                gameItem.getResult() == null,
                () -> new InvalidDataException("Game already has a result type " + gameItem.getResult())
        );
        FrjConditions.checkArg(
                gameItem.getWinnerUserId() == null,
                () -> new InvalidDataException("Game already has a winner user ID " + gameItem.getWinnerUserId())
        );

        // Update
        setCompletedFieldsOnItem(gameItem, statusType, winnerUserId);
        DdbExceptionTranslator.conditionalWrite(() -> ddbAccessor.saveItem(gameItem), "Race condition while marking game as completed.");
    }

    private void setCompletedFieldsOnItem(final GameDdbItem gameItem, final GameStatusResultType statusType, @Nullable final String winnerUserId) {
        gameItem.setStatus(GameStatusType.COMPLETE);

        switch (statusType) {
            case COMPLETED_VICTORY:
                gameItem.setResult(GameResultType.VICTORY);
                gameItem.setWinnerUserId(winnerUserId);
                return;

            case COMPLETED_DRAW:
                gameItem.setResult(GameResultType.DRAW);
                return;

            case COMPLETED_FORFEIT:
                gameItem.setResult(GameResultType.ABANDONED);
                gameItem.setWinnerUserId(winnerUserId);
                return;

            default:
                throw new IllegalArgumentException("Illegal game state transition. Can't update from in-progress to " + statusType);
        }
    }
}
