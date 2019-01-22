package com.frjgames.dal.ddb.utils;

import static java.util.stream.Collectors.toList;

import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.SimpleType;
import com.frjgames.dal.models.exceptions.DataSerializationException;
import com.frjgames.dal.models.data.PaginatedResult;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import lombok.experimental.UtilityClass;

/**
 * This is responsible for encoding/decoding pagination objects returned by DynamoDB SDK.
 *
 * @author fridge
 */
@UtilityClass
public class DdbPaginationMapper {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final JavaType SERIALIZED_TYPE = MapType.construct(
            Map.class,
            SimpleType.construct(String.class),
            SimpleType.construct(AttributeValue.class)
    );

    /**
     * Converts a DynamoDB {@link QueryResultPage} into a List of domain objects with an encoded pagination token.
     */
    public static <T, U> PaginatedResult<U> makePaginatedResult(final QueryResultPage<T> resultPage, final Function<T, U> transformer) {
        List<U> results = resultPage.getResults()
                .stream()
                .map(transformer)
                .collect(toList());

        Map<String, AttributeValue> lastEvaluatedKey = resultPage.getLastEvaluatedKey();

        return new PaginatedResult<>(results, encodePaginationToken(lastEvaluatedKey));
    }

    private static String encodePaginationToken(final Map<String, AttributeValue> lastEvaluatedKey) {
        if (lastEvaluatedKey == null) {
            return null;
        }

        try {
            return OBJECT_MAPPER.writeValueAsString(lastEvaluatedKey);
        } catch (JsonProcessingException e) {
            throw new DataSerializationException("Failed to create pagination token.", e);
        }
    }

    /**
     * Converts a pagination token returned from {@link #makePaginatedResult(QueryResultPage, Function)} into a DynamoDB pagination key.
     */
    public static Optional<Map<String, AttributeValue>> extractLastEvaluatedKey(final String paginationToken) {
        if (paginationToken == null) {
            return Optional.empty();
        }

        return decodePaginationToken(paginationToken);
    }

    private static Optional<Map<String, AttributeValue>> decodePaginationToken(final String paginationToken) {
        try {
            return Optional.of(OBJECT_MAPPER.readValue(paginationToken, SERIALIZED_TYPE));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid pagination token.", e);
        }
    }
}
