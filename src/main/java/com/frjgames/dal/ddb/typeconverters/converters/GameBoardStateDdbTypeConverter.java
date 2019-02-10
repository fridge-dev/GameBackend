package com.frjgames.dal.ddb.typeconverters.converters;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.frjgames.dal.ddb.typeconverters.types.GameBoardPlayerDdbType;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Nested converter of {@link GameBoardPlayerDdbTypeConverter}.
 *
 * @author fridge
 */
public class GameBoardStateDdbTypeConverter implements DynamoDBTypeConverter<List<List<Integer>>, List<List<GameBoardPlayerDdbType>>> {

    private static final GameBoardPlayerDdbTypeConverter INTERNAL_CONVERTER = new GameBoardPlayerDdbTypeConverter();

    @Override
    public List<List<Integer>> convert(final List<List<GameBoardPlayerDdbType>> appType) {
        return appType.stream()
                .map(internalList -> internalList.stream()
                        .map(INTERNAL_CONVERTER::convert)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    @Override
    public List<List<GameBoardPlayerDdbType>> unconvert(final List<List<Integer>> ddbType) {
        return ddbType.stream()
                .map(internalList -> internalList.stream()
                        .map(INTERNAL_CONVERTER::unconvert)
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }
}
