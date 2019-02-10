package com.frjgames.dal.ddb.typeconverters.converters;

import static org.junit.Assert.assertEquals;

import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbTypeConverterValidator;
import com.frjgames.dal.ddb.typeconverters.types.WorldLevelDdbType;
import org.junit.Test;

/**
 * Tests the {@link WorldLevelDdbTypeConverter} class.
 *
 * @author fridge
 */
public class WorldLevelDdbTypeConverterTest {

    private static final String CONVERTED_WORLD_LEVEL = "1234-hello::2345-hello";
    private static final WorldLevelDdbType WORLD_LEVEL = WorldLevelDdbType.builder()
            .worldId("1234-hello")
            .levelId("2345-hello")
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

        assertEquals(CONVERTED_WORLD_LEVEL, converted);
    }

    @Test
    public void unconvert() throws Exception {
        WorldLevelDdbType worldLevel = converter.unconvert(CONVERTED_WORLD_LEVEL);

        assertEquals(WORLD_LEVEL, worldLevel);
    }

}