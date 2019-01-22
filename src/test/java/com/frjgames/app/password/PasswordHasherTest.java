package com.frjgames.app.password;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import com.frjgames.app.password.algorithms.PasswordHashingAlgorithm;
import com.frjgames.app.password.models.PasswordHashParams;
import com.frjgames.app.password.utils.HashParamsEncoder;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Tests the {@link PasswordHasher} class.
 *
 * @author fridge
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordHasherTest {

    @InjectMocks
    private PasswordHasher passwordHasher;

    @Mock
    private PasswordHashingAlgorithm injectedPasswordHashingAlgorithm;

    @Mock
    private HashParamsEncoder injectedHashParamsEncoder;

    private String password;
    private byte[] hashedPasswordBytes;
    private String encodedHash;

    @Before
    public void setup() throws Exception {
        resetTestData();
        stubPasswordHashingAlgorithm();
    }

    private void resetTestData() {
        password = RandomStringUtils.random(10);
        hashedPasswordBytes = randomBytes(10);
        encodedHash = RandomStringUtils.random(20);
    }

    private void stubPasswordHashingAlgorithm() throws Exception {
        when(injectedPasswordHashingAlgorithm.newHashParams()).thenReturn(PasswordHashParams.builder().build());
        when(injectedPasswordHashingAlgorithm.hash(eq(password), any())).thenReturn(hashedPasswordBytes);
    }

    private void stubHashDecoder(final byte[] hash) throws Exception {
        PasswordHashParams params = PasswordHashParams.builder()
                .hash(hash)
                .build();
        when(injectedHashParamsEncoder.decodeHash(encodedHash)).thenReturn(params);
    }

    @Test
    public void createStorableHash() throws Exception {
        ArgumentCaptor<PasswordHashParams> captor = ArgumentCaptor.forClass(PasswordHashParams.class);
        when(injectedHashParamsEncoder.encodeHash(captor.capture())).thenReturn(encodedHash);

        String storableHash = passwordHasher.createStorableHash(password);

        assertEquals(encodedHash, storableHash);
        assertEquals(hashedPasswordBytes, captor.getValue().getHash());
    }

    @Test
    public void matches_CorrectPassword() throws Exception {
        stubHashDecoder(hashedPasswordBytes);

        boolean matches = passwordHasher.matches(password, encodedHash);

        assertTrue(matches);
    }

    @Test
    public void matches_IncorrectPassword_BytesSameLength() throws Exception {
        stubHashDecoder(randomBytes(hashedPasswordBytes.length));

        boolean matches = passwordHasher.matches(password, encodedHash);

        assertFalse(matches);
    }

    @Test
    public void matches_IncorrectPassword_BytesDiffLength() throws Exception {
        stubHashDecoder(randomBytes(hashedPasswordBytes.length * 2));

        boolean matches = passwordHasher.matches(password, encodedHash);

        assertFalse(matches);
    }

    private byte[] randomBytes(final int size) {
        byte[] bytes = new byte[size];
        new Random().nextBytes(bytes);
        return bytes;
    }
}