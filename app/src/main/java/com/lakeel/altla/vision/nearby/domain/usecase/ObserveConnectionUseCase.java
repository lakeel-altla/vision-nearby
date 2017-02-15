package com.lakeel.altla.vision.nearby.domain.usecase;

import com.lakeel.altla.vision.nearby.data.repository.firebase.ConnectedRepository;
import com.lakeel.altla.vision.nearby.data.repository.firebase.UserConnectionRepository;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

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

    public void execute() {
        String userId = CurrentUser.getUid();

        connectedRepository.observe()
                .subscribeOn(Schedulers.io())
                .subscribe(() -> {
                    // Called when connected token firebase.
                    // Set presence token online.
                    userConnectionRepository.saveOnline(userId);
                });

        // Change presence token offline when disconnect token firebase.
        userConnectionRepository.saveOfflineOnDisconnect(userId);
    }
}
