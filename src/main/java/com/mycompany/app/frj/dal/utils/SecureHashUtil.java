package com.mycompany.app.frj.dal.utils;

import java.io.UnsupportedEncodingException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.Md5Crypt;

/**
 * TODO
 *
 * @author alecva
 */
@RequiredArgsConstructor
public class SecureHashUtil {

    private final String internalSalt;

    public String hash(final String payload, final String salt) {
        try {
            return applyHash(payload + salt);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("TODO make better exception", e);
        }
    }

    private String applyHash(final String saltedPayload) throws UnsupportedEncodingException {
        return Md5Crypt.md5Crypt(saltedPayload.getBytes("UTF-8"));
    }
}
