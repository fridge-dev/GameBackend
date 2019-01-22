package com.frjgames.app.password.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import com.frjgames.app.password.models.PasswordHashParams;
import com.frjgames.app.password.models.AlgorithmType;
import com.frjgames.app.password.models.InvalidHashException;
import com.frjgames.app.password.models.PasswordHashParams.PasswordHashParamsBuilder;
import com.frjgames.testutils.TestUtilExceptionValidator;
import java.security.SecureRandom;
import java.util.Random;
import java.util.function.Consumer;
import org.junit.Test;

/**
 * Tests the {@link HashParamsEncoder} class.
 *
 * @author fridge
 */
public class HashParamsEncoderTest {

    private HashParamsEncoder encoder = new HashParamsEncoder();

    @Test
    public void encodeHash_DecodeHash() throws Exception {
        for (int i = 0; i < 50; i++) {
            inverseTest(i);
        }
    }

    private void inverseTest(final int iteration) throws Exception {
        PasswordHashParams params = newRandomParams();

        String failureMessage = String.format("Iteration %s failed with params: %s", iteration, params);
        try {
            assertEquals(failureMessage, params, encoder.decodeHash(encoder.encodeHash(params)));
        } catch (Exception e) {
            throw new RuntimeException(failureMessage, e);
        }
    }

    @Test
    public void encodeHash_Idempotent() throws Exception {
        PasswordHashParams params = newRandomParams();

        String digest1 = encoder.encodeHash(params);
        String digest2 = encoder.encodeHash(params);

        assertEquals(digest1, digest2);
    }

    @Test
    public void encodeHash_UniqueResults() throws Exception {
        PasswordHashParams params1 = newRandomParams();
        PasswordHashParams params2 = newRandomParams();

        String digest1 = encoder.encodeHash(params1);
        String digest2 = encoder.encodeHash(params2);

        assertNotEquals(digest1, digest2);
    }

    @Test
    public void decodeHash_Idempotent() throws Exception {
        String encodedHash = encoder.encodeHash(newRandomParams());
        PasswordHashParams decodedParams1 = encoder.decodeHash(encodedHash);
        PasswordHashParams decodedParams2 = encoder.decodeHash(encodedHash);

        assertEquals(decodedParams1, decodedParams2);
    }

    @Test
    public void encodeHash_InvalidInput() throws Exception {
        validateIllegalArg_EncodeHash(builder -> builder.algorithm(null));
        validateIllegalArg_EncodeHash(builder -> builder.iterations(0));
        validateIllegalArg_EncodeHash(builder -> builder.iterations(-1));
        validateIllegalArg_EncodeHash(builder -> builder.salt(null));
        validateIllegalArg_EncodeHash(builder -> builder.salt(new byte[0]));
        validateIllegalArg_EncodeHash(builder -> builder.hash(null));
        validateIllegalArg_EncodeHash(builder -> builder.hash(new byte[0]));
        validateIllegalArg_EncodeHash(builder -> builder.hashLength(0));
        validateIllegalArg_EncodeHash(builder -> builder.hashLength(-1));
        validateIllegalArg_EncodeHash(builder -> builder.hash(new byte[5]).hashLength(4));
    }

    private void validateIllegalArg_EncodeHash(final Consumer<PasswordHashParamsBuilder> modifier) throws Exception {
        TestUtilExceptionValidator.validateThrown(InvalidHashException.class, () -> encoder.encodeHash(newRandomParams(modifier)));
    }

    @Test
    public void decodeHash_InvalidInput() throws Exception {
        // Simple cases
        validateIllegalArg_DecodeHash(null);
        validateIllegalArg_DecodeHash("");
        validateIllegalArg_DecodeHash(" ");

        // Validate that this case doesn't throw exception, since we build all invalid input cases from this example.
        String encoded = encoder.encodeHash(newRandomParams());
        String encodedSalt = encoded.split(":")[3];
        String encodedHashLen = encoded.split(":")[4];
        String encodedHash = encoded.split(":")[5];
        encoder.decodeHash(String.format("1:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Format
        validateIllegalArg_DecodeHash(String.format(":0:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format(":PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("0:PBKDF2SHA1:123:%s:%s:%s:", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("0:PBKDF2SHA1:123:%s:%s", encodedSalt, encodedHashLen));

        // Version
        validateIllegalArg_DecodeHash(String.format(":PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("0:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("2:PBKDF2SHA1:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Algorithm
        validateIllegalArg_DecodeHash(String.format("1::123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:MD5Hahaa:123:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Iterations
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1::%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:0:%s:%s:%s", encodedSalt, encodedHashLen, encodedHash));

        // Salt
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123::%s:%s", encodedHash, encodedHashLen));

        // Hash length
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s::%s", encodedSalt, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:0:%s", encodedSalt, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:1:%s", encodedSalt, encodedHash));
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:1000:%s", encodedSalt, encodedHash));

        // Hash
        validateIllegalArg_DecodeHash(String.format("1:PBKDF2SHA1:123:%s:%s:", encodedSalt, encodedHashLen));
    }

    private void validateIllegalArg_DecodeHash(final String encodedHash) throws Exception {
        TestUtilExceptionValidator.validateThrown(InvalidHashException.class, () -> encoder.decodeHash(encodedHash));
    }

    private PasswordHashParams newRandomParams(final Consumer<PasswordHashParamsBuilder> modifier) {
        PasswordHashParamsBuilder paramBuilder = newRandomParams().toBuilder();

        modifier.accept(paramBuilder);

        return paramBuilder.build();
    }

    private PasswordHashParams newRandomParams() {
        byte[] hash = randomBytes(50);

        return PasswordHashParams.builder()
                .algorithm(AlgorithmType.PBKDF2SHA1)
                .iterations(randomInt())
                .salt(randomBytes(30))
                .hash(hash)
                .hashLength(hash.length)
                .build();
    }

    private int randomInt() {
        // Guarantee positive number
        return Math.abs(new Random().nextInt()) + 1;
    }

    private byte[] randomBytes(final int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return bytes;
    }

}