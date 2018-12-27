package com.frjgames.dal.ddb.utils;

import com.frjgames.dal.models.PaginatedResult;
import java.util.List;
import java.util.Optional;
import lombok.Data;

/**
 * A simple POJO implementation.
 *
 * @author fridge
 */
@Data
public class PaginatedResultImpl<T> implements PaginatedResult<T> {

    private final List<T> results;

    private final String paginationToken;

    public Optional<String> getPaginationToken() {
        return Optional.ofNullable(this.paginationToken);
    }
}
