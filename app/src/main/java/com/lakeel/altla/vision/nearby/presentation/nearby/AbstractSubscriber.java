package com.lakeel.altla.vision.nearby.presentation.nearby;

import com.lakeel.altla.library.ResolutionResultCallback;

public abstract class AbstractSubscriber {

    public abstract void subscribe(ResolutionResultCallback callback);

    public abstract void unSubscribe(ResolutionResultCallback callback);
}
