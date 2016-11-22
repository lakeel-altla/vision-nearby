package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresenceRepository;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public final class ObserveConnectionUseCase {

    @Inject
    FirebaseConnectionRepository mConnectionRepository;

    @Inject
    FirebasePresenceRepository mPresenceRepository;

    @Inject
    public ObserveConnectionUseCase() {
    }

    public void execute() {
        mConnectionRepository
                .observeConnectivity()
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                    mPresenceRepository.savePresenceOnline();
                });

        mPresenceRepository.savePresenceOfflineOnDisconnected();
    }
}
