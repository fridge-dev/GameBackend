package com.frjgames.dal.models.data;

import com.frjgames.dal.models.enums.GameStatusResultType;
import java.util.Optional;
import javax.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

/**
 * The model of a game that is in progress or completed.
 *
 * @see MatchMadeGame
 *
 * @author fridge
 */
@Data
@Builder(toBuilder = true)
public class Game implements AppDataModel {

    private final String gameId;

    private final String hostUserId;

    private final String guestUserId;

    private final GameStatusResultType status;

    @Nullable
    private final String winnerUserId;

    public Optional<String> getWinnerUserId() {
        return Optional.ofNullable(winnerUserId);
    }
}
