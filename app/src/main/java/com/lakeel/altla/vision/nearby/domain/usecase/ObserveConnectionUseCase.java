package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.ConnectedRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserConnectionRepository;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public final class ObserveConnectionUseCase {

    @Inject
    ConnectedRepository connectedRepository;

    @Inject
    UserConnectionRepository userConnectionRepository;

    @Inject
    ObserveConnectionUseCase() {
    }

    public void execute(String userId) {
        connectedRepository.observe()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    // Called when connected to firebase.
                    // Set presence to online.
                    userConnectionRepository.saveOnline(userId);
                });

        // Change presence to offline when disconnect to firebase.
        userConnectionRepository.saveOfflineOnDisconnect(userId);
    }
}
