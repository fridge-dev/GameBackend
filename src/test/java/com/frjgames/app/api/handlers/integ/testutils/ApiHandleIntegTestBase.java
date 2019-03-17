package com.frjgames.app.api.handlers.integ.testutils;

import com.frjgames.app.api.config.ApiHandlerModule;
import com.frjgames.dal.ddb.items.DdbItem;
import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbLocalTestBase;
import lombok.AccessLevel;
import lombok.Getter;
import org.junit.Before;

/**
 * Test utility to be re-used by any test that is func-testing an {@link com.frjgames.app.api.models.interfaces.ApiHandler}.
 *
 * @author fridge
 */
public abstract class ApiHandleIntegTestBase<T extends DdbItem> extends TestUtilDynamoDbLocalTestBase<T> {

    @Getter(AccessLevel.PROTECTED)
    private ApiHandlerModule apiHandlerModule;

    public ApiHandleIntegTestBase(Class<T> clazz) {
        super(clazz);
    }

    @SafeVarargs
    public ApiHandleIntegTestBase(Class<T> clazz, Class<? extends DdbItem>... tablesToCreate) {
        super(clazz, tablesToCreate);
    }

    @Before
    public void beforeApiHandleIntegTestBase() {
        apiHandlerModule = ApiHandlerModule.instantiateModule(super.getDalModule());
    }

}
