package com.lakeel.altla.vision.nearby.presentation.uri;

import com.lakeel.altla.vision.nearby.core.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

import static com.lakeel.altla.vision.nearby.presentation.uri.UriChecker.Result.EMPTY;
import static com.lakeel.altla.vision.nearby.presentation.uri.UriChecker.Result.OK;
import static com.lakeel.altla.vision.nearby.presentation.uri.UriChecker.Result.SYNTAX_ERROR;

public class UriChecker {

    public enum Result {
        OK, EMPTY, SYNTAX_ERROR
    }

    private final String uri;

    public UriChecker(String uri) {
        this.uri = uri;
    }

    public Result check() {
        if (StringUtils.isEmpty(uri)) {
            return EMPTY;
        }

        try {
            new URI(uri);
        } catch (URISyntaxException e) {
            return SYNTAX_ERROR;
        }

        return OK;
    }
}
