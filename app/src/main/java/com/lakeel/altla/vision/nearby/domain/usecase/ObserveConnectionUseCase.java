package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.ConnectedRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserConnectionRepository;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public final class ObserveConnectionUseCase {

    @Inject
    ConnectedRepository connectionRepository;

    @Inject
    UserConnectionRepository presencesRepository;

    @Inject
    ObserveConnectionUseCase() {
    }

    public void execute(String userId) {
        connectionRepository.observe()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    // Called when connected to firebase.
                    // Set presence to online.
                    presencesRepository.saveOnline(userId);
                });

        // Change presence to offline when disconnect to firebase.
        presencesRepository.saveOfflineOnDisconnect(userId);
    }
}
