package com.frjgames.dal.ddb.utils;

import static java.util.stream.Collectors.toList;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.frjgames.dal.models.PaginatedResult;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * TODO
 *
 * @author fridge
 */
public class DdbPaginationMapper {

    public static <T, U> PaginatedResult<U> makePaginedResult(final QueryResultPage<T> resultPage, final Function<T, U> transformer) {
        List<U> results = resultPage.getResults()
                .stream()
                .map(transformer)
                .collect(toList());

        Map<String, AttributeValue> lastEvaluatedKey = resultPage.getLastEvaluatedKey();

        return new PaginatedResultImpl<>(results, serialize(lastEvaluatedKey));
    }

    private static String serialize(final Map<String, AttributeValue> lastEvaluatedKey) {
        return "TODO";
    }

}
