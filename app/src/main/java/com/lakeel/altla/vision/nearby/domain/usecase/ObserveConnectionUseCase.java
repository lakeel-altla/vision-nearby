package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionRepository;
import com.lakeel.altla.vision.nearby.data.repository.FirebaseConnectionsRepository;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public final class ObserveConnectionUseCase {

    @Inject
    FirebaseConnectionRepository connectionRepository;

    @Inject
    FirebaseConnectionsRepository presencesRepository;

    @Inject
    ObserveConnectionUseCase() {
    }

    public void execute(String userId) {
        connectionRepository.observeConnection()
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
