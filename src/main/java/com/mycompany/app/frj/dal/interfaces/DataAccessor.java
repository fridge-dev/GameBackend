package com.mycompany.app.frj.dal.interfaces;

import com.mycompany.app.frj.dal.models.AppDataModel;
import com.mycompany.app.frj.dal.models.keys.AppDataKey;
import java.util.Optional;

/**
 * Generic accessor for Data Access Layer (DAL).
 *
 * Less generic access patterns (batch/paginated reads, updates) should not go here, and instead,
 * in the specific extension of this interface.
 *
 * @author alecva
 */
public interface DataAccessor<K extends AppDataKey, V extends AppDataModel> {

    void create(V data);

    Optional<V> load(K key);

}
