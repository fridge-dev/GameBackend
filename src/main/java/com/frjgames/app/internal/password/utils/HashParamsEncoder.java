package com.frjgames.app.internal.password.utils;

import com.frjgames.app.internal.password.models.InvalidHashException;
import com.frjgames.app.internal.password.models.PasswordHashParams;
import com.google.common.base.Preconditions;
import com.frjgames.app.internal.password.models.AlgorithmType;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Utility class for encoding/decoding a {@link PasswordHashParams} to/from a string that can be persisted.
 *
 * Supports versioning, but currently there is only 1 version.
 *
 * @author fridge
 */
public class HashParamsEncoder {

    private static final String VERSION = "1";

    private static final String DELIMITER = ":";

    private static final int INDEX_VERSION = 0;
    private static final int INDEX_ALGORITHM = 1;
    private static final int INDEX_ITERATIONS = 2;
    private static final int INDEX_SALT = 3;
    private static final int INDEX_HASH_LEN = 4;
    private static final int INDEX_HASH = 5;
    private static final int NUM_SPLITS = 6;

    /**
     * Encode the hash digest and configuration params into a single String that can be used for persistence.
     */
    public String encodeHash(final PasswordHashParams params) throws InvalidHashException {
        String[] encodedParams = new String[NUM_SPLITS];
        encodedParams[INDEX_VERSION] = VERSION;
        encodedParams[INDEX_ALGORITHM] = encodeAlgorithm(params.getAlgorithm());
        encodedParams[INDEX_ITERATIONS] = encodeInteger(params.getIterations());
        encodedParams[INDEX_SALT] = encodeBytes(params.getSalt());
        encodedParams[INDEX_HASH_LEN] = encodeInteger(params.getHashLength());
        encodedParams[INDEX_HASH] = encodeBytes(params.getHash());

        checkArg(params.getHash().length == params.getHashLength(), "Hash digest length mismatch.");

        return String.join(DELIMITER, encodedParams);
    }

    /**
     * Decode the encoded hash digest and configuration params from the encoding performed in {@link #encodeHash(PasswordHashParams)}.
     */
    public PasswordHashParams decodeHash(final String encodedHash) throws InvalidHashException {
        checkArg(StringUtils.isNotBlank(encodedHash), "Can't decode a blank hash digest.");

        String[] split = encodedHash.split(DELIMITER);
        checkArg(split.length == NUM_SPLITS, "Encoded hash has incorrect number of parts.");

        // Validate version
        String version = split[INDEX_VERSION];
        checkArg(VERSION.equals(split[INDEX_VERSION]), "Unsupported encoded hash version %s", version);

        // Params
        AlgorithmType algorithm = decodeAlgorithm(split[INDEX_ALGORITHM]);
        int iterations = decodeInteger(split[INDEX_ITERATIONS]);
        byte[] salt = decodeBytes(split[INDEX_SALT]);
        int hashLen = decodeInteger(split[INDEX_HASH_LEN]);
        byte[] hash = decodeBytes(split[INDEX_HASH]);

        checkArg(hash.length == hashLen, "Hash digest length mismatch.");

        return PasswordHashParams.builder()
                .algorithm(algorithm)
                .iterations(iterations)
                .salt(salt)
                .hashLength(hashLen)
                .hash(hash)
                .build();
    }

    private String encodeAlgorithm(final AlgorithmType algorithm) throws InvalidHashException {
        checkArg(null != algorithm, "Missing algorithm type.");
        return algorithm.name();
    }

    private AlgorithmType decodeAlgorithm(final String algorithmStr) throws InvalidHashException {
        AlgorithmType algorithm = EnumUtils.getEnum(AlgorithmType.class, algorithmStr);
        checkArg(null != algorithm, "Invalid hash algorithm '%s' in the encoded string", algorithmStr);

        return algorithm;
    }

    private String encodeInteger(final int integer) throws InvalidHashException {
        checkArg(integer > 0, "Integer must be greater than 0.");
        return Integer.toString(integer);
    }

    private int decodeInteger(final String intString) throws InvalidHashException {
        int i;
        try {
            i = Integer.parseInt(intString);
        } catch (NumberFormatException e) {
            throw new InvalidHashException(String.format("'%s' is not a valid int.", intString), e);
        }

        checkArg(i > 0, "Integer decoded from string must be greater than 0.");

        return i;
    }

    private String encodeBytes(final byte[] bytes) throws InvalidHashException {
        checkArg(ArrayUtils.isNotEmpty(bytes), "Bytes array must be non-empty.");

        // XML datatype converter seems to be only way (I could find) to ensure that encoded bytes never contains the delimiter
        String encodedBytes = DatatypeConverter.printBase64Binary(bytes);
        Preconditions.checkState(!encodedBytes.contains(DELIMITER), "Internal failure: The encoded bytes illegally contain the hashing delimiter character.");

        return encodedBytes;
    }

    private byte[] decodeBytes(final String string) throws InvalidHashException {
        checkArg(StringUtils.isNotBlank(string), "Can't decode blank string into bytes.");
        return DatatypeConverter.parseBase64Binary(string);
    }

    private void checkArg(final boolean condition, final String message, final Object... args) throws InvalidHashException {
        if (!condition) {
            throw new InvalidHashException(String.format(message, args));
        }
    }
}
