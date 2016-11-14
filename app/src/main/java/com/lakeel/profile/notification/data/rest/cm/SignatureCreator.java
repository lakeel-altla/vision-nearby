package com.lakeel.profile.notification.data.rest.cm;

import com.lakeel.profile.notification.data.execption.SignatureCreateException;

import org.apache.commons.codec.binary.Base64;

import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class SignatureCreator {

    private static final String ALGORITHM_SHA256 = "HmacSHA256";

    private String mSignatureString;

    private String mSecretKey;

    public SignatureCreator(String signatureString, String secretKey) {
        mSignatureString = signatureString;
        mSecretKey = secretKey;
    }

    public String createEncodedSignature() {
        Mac mac;
        try {
            mac = Mac.getInstance(ALGORITHM_SHA256);
            SecretKeySpec secret_key = new SecretKeySpec(mSecretKey.getBytes(), ALGORITHM_SHA256);
            mac.init(secret_key);
        } catch (GeneralSecurityException e) {
            throw new SignatureCreateException(mSignatureString);
        }

        return new String(Base64.encodeBase64(mac.doFinal(mSignatureString.getBytes())));
    }
}
