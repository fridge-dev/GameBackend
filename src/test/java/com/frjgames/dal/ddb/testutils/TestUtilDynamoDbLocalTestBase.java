package com.frjgames.dal.ddb.testutils;

import com.frjgames.dal.config.DataAccessLayerModule;
import com.frjgames.dal.config.DataAccessLayerModuleFactory;
import com.frjgames.dal.ddb.items.DdbItem;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.Before;

/**
 * Test utility to be re-used by any test that needs to create a local instance of DynamoDB.
 *
 * @author fridge
 */
public abstract class TestUtilDynamoDbLocalTestBase<T extends DdbItem> {

    private final Set<Class<? extends DdbItem>> itemsToCreate;

    public TestUtilDynamoDbLocalTestBase(final Class<T> clazz) {
        this.itemsToCreate = Collections.singleton(clazz);
    }

    @SafeVarargs
    public TestUtilDynamoDbLocalTestBase(final Class<T> clazz, final Class<? extends DdbItem>... tablesToCreate) {
        this.itemsToCreate = new HashSet<>();
        this.itemsToCreate.add(clazz);
        this.itemsToCreate.addAll(Arrays.asList(tablesToCreate));
    }

    @Getter(AccessLevel.PROTECTED)
    private DataAccessLayerModule dalModule;

    @Before
    public void superBefore() {
        dalModule = DataAccessLayerModuleFactory.getModuleLocal(itemsToCreate);
    }

}
