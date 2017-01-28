package com.lakeel.altla.vision.nearby.data.mapper.model;

import com.lakeel.altla.vision.nearby.domain.model.Token;

public final class TokenMapper {

    public Token map(String userId, String beaconId, String tokenValue) {
        Token token = new Token();
        token.userId = userId;
        token.beaconId = beaconId;
        token.token = tokenValue;
        return token;
    }
}
