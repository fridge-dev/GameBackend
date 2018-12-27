package com.frjgames.dal.models;

import java.util.List;
import java.util.Optional;

/**
 * Represents a page of results in a paginated call pattern.
 *
 * @author fridge
 */
public interface PaginatedResult<T> {

    /**
     * A single page of results.
     */
    List<T> getResults();

    /**
     * The pagination token to be used on the next request.
     */
    Optional<String> getPaginationToken();

}
