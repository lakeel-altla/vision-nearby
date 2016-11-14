package com.lakeel.profile.notification.domain.usecase;

import com.lakeel.profile.notification.domain.repository.FirebaseConnectionRepository;
import com.lakeel.profile.notification.domain.repository.FirebasePresenceRepository;

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
