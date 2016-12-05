package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.domain.repository.FirebaseConnectionRepository;
import com.lakeel.altla.vision.nearby.domain.repository.FirebasePresencesRepository;

import javax.inject.Inject;

import rx.schedulers.Schedulers;

public final class ObservePresenceUseCase {

    @Inject
    FirebaseConnectionRepository connectionRepository;

    @Inject
    FirebasePresencesRepository presencesRepository;

    @Inject
    public ObservePresenceUseCase() {
    }

    public void execute(String userId) {
        connectionRepository
                .observeConnected()
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                    // Called when connected to firebase. Change presence to online.
                    presencesRepository.savePresenceOnline(userId);
                });

        // Change presence to offline when disconnect to firebase.
        presencesRepository.savePresenceOfflineOnDisconnected(userId);
    }
}
