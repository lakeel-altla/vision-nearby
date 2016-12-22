package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.cm.resource.Timestamp;
import com.lakeel.altla.vision.nearby.domain.repository.RestCmRepository;

import javax.inject.Inject;

import rx.Single;

public final class SendMessageUseCase {

    @Inject
    RestCmRepository repository;

    @Inject
    SendMessageUseCase() {
    }

    public Single<Timestamp> execute(String jid, String message) {
        return repository.sendMessage(jid, message);
    }
}
