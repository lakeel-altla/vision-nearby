package com.lakeel.altla.vision.nearby.presentation.nearby;

import com.lakeel.altla.library.ResolutionResultCallback;

public interface Subscriber {

    void subscribe(ResolutionResultCallback callback);

    void unSubscribe(ResolutionResultCallback callback);
}
