package com.frjgames.dal.models.data;

import java.util.List;
import java.util.Optional;
import lombok.Data;

/**
 * A POJO for encapsulating a list of results of a single "page" and a pagination token to be
 * used to retrieve the next "page" of results.
 *
 * @author fridge
 */
@Data
public class PaginatedResult<T> {

    /**
     * A single page of results.
     */
    private final List<T> results;

    /**
     * The pagination token to be used on the next request.
     */
    private final String paginationToken;

    public Optional<String> getPaginationToken() {
        return Optional.ofNullable(this.paginationToken);
    }
}
