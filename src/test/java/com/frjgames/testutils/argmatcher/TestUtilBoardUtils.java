package com.frjgames.testutils.argmatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.RandomUtils;

/**
 * Test utility for related to the game board.
 *
 * @author fridge
 */
public class TestUtilBoardUtils {

    public static <T extends Enum<T>> List<List<T>> generateRandomBoard(final Class<T> enumClass, final int cellsPerBoard) {
        Set<T> enumSet = EnumSet.allOf(enumClass);

        return generateRandomBoard(enumSet, cellsPerBoard);
    }

    /**
     * Generate a random instance of a board.
     */
    public static <T> List<List<T>> generateRandomBoard(final Collection<T> source, final int cellsPerBoard) {
        List<List<T>> output = new ArrayList<>();

        for (int i = 0; i < cellsPerBoard; i++) {
            List<T> innerBoard = new ArrayList<>();
            for (int j = 0; j < cellsPerBoard; j++) {
                innerBoard.add(getRandom(source));
            }
            output.add(innerBoard);
        }

        return output;
    }

    private static <T> T getRandom(final Collection<T> source) {
        int rand = RandomUtils.nextInt(0, source.size());

        return source.stream()
                .skip(rand)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Didn't expect to get here."));
    }
}
