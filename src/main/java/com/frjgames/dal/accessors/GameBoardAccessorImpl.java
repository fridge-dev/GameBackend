package com.frjgames.dal.accessors;

import com.frjgames.dal.accessors.fieldmappers.GameBoardStateMapper;
import com.frjgames.dal.ddb.accessors.DynamoDbAccessor;
import com.frjgames.dal.ddb.items.GameBoardDdbItem;
import com.frjgames.dal.ddb.utils.DdbExceptionTranslator;
import com.frjgames.dal.models.data.GameBoard;
import com.frjgames.dal.models.enums.PlayerEnum;
import com.frjgames.dal.models.exceptions.DataAccessLayerException;
import com.frjgames.dal.models.interfaces.GameBoardAccessor;
import com.frjgames.dal.models.keys.GameIdKey;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

/**
 * Accesses the game board DynamoDB.
 *
 * @author fridge
 */
@RequiredArgsConstructor
public class GameBoardAccessorImpl implements GameBoardAccessor {

    private final DynamoDbAccessor<GameBoardDdbItem> ddbAccessor;

    @Override
    public void create(final GameBoard gameBoard) throws DataAccessLayerException {
        GameBoardDdbItem item = domainTypeToItem(gameBoard);

        DdbExceptionTranslator.conditionalWrite(() -> ddbAccessor.saveItem(item), "Cannot create game, it already exists.");
    }

    private GameBoardDdbItem domainTypeToItem(final GameBoard gameBoard) {
        GameBoardDdbItem item = new GameBoardDdbItem();
        item.setGameId(gameBoard.getGameId());
        item.setGameBoardState(GameBoardStateMapper.convertBoardForPersistence(gameBoard.getGameBoardState()));
        item.setNextMove(GameBoardStateMapper.convertPlayer(gameBoard.getNextMove()));
        item.setNextBoardIndex(gameBoard.getNextBoardIndex());

        return item;
    }

    @Override
    public Optional<GameBoard> load(final GameIdKey key) throws DataAccessLayerException {
        return ddbAccessor.loadItem(key.getGameId())
                .map(this::itemToDomainType);
    }

    private GameBoard itemToDomainType(final GameBoardDdbItem item) {
        PlayerEnum nextMove = GameBoardStateMapper.unconvertPlayer(item.getNextMove());

        return GameBoard.builder()
                .gameId(item.getGameId())
                .gameBoardState(GameBoardStateMapper.unconvertBoardForAppLayer(item.getGameBoardState()))
                .nextMove(nextMove)
                .nextBoardIndex(item.getNextBoardIndex())
                .boardVersion(encodeLockVersion(item.getOptimisticLockingVersion()))
                .build();
    }

    private String encodeLockVersion(final long l) {
        return Long.toString(l);
    }

    private Long decodeLockVersion(final String s) {
        return Long.valueOf(s);
    }

    @Override
    public void updateBoardWithMove(final GameBoard gameBoard) throws DataAccessLayerException {
        GameBoardDdbItem item = domainTypeToItem(gameBoard);
        item.setOptimisticLockingVersion(decodeLockVersion(gameBoard.getBoardVersion()));

        DdbExceptionTranslator.conditionalWrite(() -> ddbAccessor.saveItem(item), "Trying to update old version of game.");
    }
}
