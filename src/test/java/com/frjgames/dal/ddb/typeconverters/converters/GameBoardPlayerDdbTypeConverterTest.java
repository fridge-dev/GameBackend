package com.frjgames.dal.ddb.typeconverters.converters;

import static org.junit.Assert.assertEquals;

import com.frjgames.dal.ddb.testutils.TestUtilDynamoDbTypeConverterValidator;
import com.frjgames.dal.ddb.typeconverters.types.GameBoardPlayerDdbType;
import com.frjgames.testutils.TestUtilExceptionValidator;
import org.junit.Test;

/**
 * Tests the {@link GameBoardPlayerDdbTypeConverter} class.
 *
 * @author fridge
 */
public class GameBoardPlayerDdbTypeConverterTest {

    private GameBoardPlayerDdbTypeConverter converter = new GameBoardPlayerDdbTypeConverter();

    @Test
    public void inverseProperties() throws Exception {
        TestUtilDynamoDbTypeConverterValidator.testConverterInverseProperties(
                converter,
                1,
                GameBoardPlayerDdbType.PLAYER_TWO
        );
    }

    @Test
    public void convert() throws Exception {
        assertEquals(1, (int) converter.convert(GameBoardPlayerDdbType.PLAYER_ONE));
        assertEquals(2, (int) converter.convert(GameBoardPlayerDdbType.PLAYER_TWO));
        assertEquals(-1, (int) converter.convert(GameBoardPlayerDdbType.NEITHER));

        TestUtilExceptionValidator.assertIllegalArg(() -> converter.convert(null));
    }

    @Test
    public void unconvert() throws Exception {
        assertEquals(GameBoardPlayerDdbType.PLAYER_ONE, converter.unconvert(1));
        assertEquals(GameBoardPlayerDdbType.PLAYER_TWO, converter.unconvert(2));
        assertEquals(GameBoardPlayerDdbType.NEITHER, converter.unconvert(-1));

        TestUtilExceptionValidator.assertIllegalArg(() -> converter.unconvert(null));
        TestUtilExceptionValidator.assertIllegalArg(() -> converter.unconvert(0));
        TestUtilExceptionValidator.assertIllegalArg(() -> converter.unconvert(3));
    }
}