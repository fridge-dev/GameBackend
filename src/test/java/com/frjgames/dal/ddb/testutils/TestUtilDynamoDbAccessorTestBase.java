package com.frjgames.dal.ddb.testutils;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.frjgames.dal.ddb.accessors.BaseDynamoDbAccessor;
import com.frjgames.dal.ddb.items.DdbItem;
import java.util.function.Function;
import org.junit.Before;

/**
 * Test utility that offers a small DRY benefit for accessors following frj pattern.
 *
 * @author alecva
 */
public abstract class TestUtilDynamoDbAccessorTestBase<T extends DdbItem, A extends BaseDynamoDbAccessor<T>> extends TestUtilDynamoDbLocalTestBase<T> {

    private Function<DynamoDBMapper, A> accessorConstructor;

    protected A accessor;

    public TestUtilDynamoDbAccessorTestBase(final Class<T> itemType, final Function<DynamoDBMapper, A> accessorConstructor) {
        super(itemType);
        this.accessorConstructor = accessorConstructor;
    }

    @Before
    public void constructAccessor() {
        accessor = accessorConstructor.apply(dynamoDbMapper);
    }

}
