package com.frjgames.dal.ddb.typeconverters.converters;

import static org.junit.Assert.assertEquals;

import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import com.frjgames.testutils.TestUtilDynamoDbTypeConverterValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the {@link WorldLevelDdbTypeConverter} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class WorldLevelDdbTypeConverterTest {

    private static final WorldLevelDdbType WORLD_LEVEL = WorldLevelDdbType.builder()
            .worldNumber(1234)
            .levelNumber(2345)
            .build();

    private WorldLevelDdbTypeConverter converter = new WorldLevelDdbTypeConverter();

    @Test
    public void inverseProperties() throws Exception {
        TestUtilDynamoDbTypeConverterValidator.testConverterInverseProperties(
                converter,
                "1::2",
                WORLD_LEVEL
        );
    }

    @Test
    public void convert() throws Exception {
        String converted = converter.convert(WORLD_LEVEL);

        assertEquals("1234::2345", converted);
    }
    @Test
    public void unconvert() throws Exception {
        WorldLevelDdbType worldLevel = converter.unconvert("1234::2345");

        assertEquals(WORLD_LEVEL, worldLevel);
    }

}