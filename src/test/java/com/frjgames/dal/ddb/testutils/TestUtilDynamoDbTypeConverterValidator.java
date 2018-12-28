package com.frjgames.dal.ddb.testutils;

import static org.junit.Assert.assertEquals;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

/**
 * Tests for utilities related to testing {@link DynamoDBTypeConverter} implementations.
 *
 * @author fridge
 */
public class TestUtilDynamoDbTypeConverterValidator {

    /**
     * Tests the basic contract of a {@link DynamoDBTypeConverter} is held: convert() and unconvert() should be inverse operations.
     *
     * It may be possible for a use case to exist where this is not true, but for most DynamoDBTypeConverters, you can extend this UT
     * class and have this tested automatically.
     *
     * @param converter An instance of the converter being tested
     * @param marshalledValue A valid example value of the S typed DynamoDB object
     * @param domainValue A valid example value of the T typed domain object
     * @param <S> DynamoDB native type
     * @param <T> Domain object type
     */
    public static <S, T> void testConverterInverseProperties(
            final DynamoDBTypeConverter<S, T> converter,
            final S marshalledValue,
            final T domainValue
    ) throws Exception {
        unconvertConvert(converter, marshalledValue);
        convertUnconvert(converter, domainValue);
    }

    /**
     * Validates convert(unconvert(Y)) == Y
     */
    private static <S, T> void unconvertConvert(final DynamoDBTypeConverter<S, T> converter, final S marshalledValue) throws Exception {
        T unmarshalledValue = converter.unconvert(marshalledValue);
        S reMarshalledValue = converter.convert(unmarshalledValue);

        assertEquals(marshalledValue, reMarshalledValue);
    }

    /**
     * Validates unconvert(convert(X)) == X
     */
    private static <S, T> void convertUnconvert(final DynamoDBTypeConverter<S, T> converter, final T domainValue) throws Exception {
        S marshalledValue = converter.convert(domainValue);
        T domainValueUnmarshalled = converter.unconvert(marshalledValue);

        assertEquals(domainValue, domainValueUnmarshalled);
    }
}
