package com.lakeel.altla.vision.nearby.domain.usecase;

import android.support.annotation.NonNull;

import com.lakeel.altla.vision.nearby.data.repository.firebase.LINELinksRepository;
import com.lakeel.altla.vision.nearby.domain.model.LineLink;
import com.lakeel.altla.vision.nearby.presentation.helper.CurrentUser;

import javax.inject.Inject;

import rx.Single;
import rx.schedulers.Schedulers;

public final class SaveLINEUrlUseCase {

    @Inject
    LINELinksRepository repository;

    @Inject
    SaveLINEUrlUseCase() {
    }

    public Single<String> execute(@NonNull String lineUrl) {
        LineLink lineLink = new LineLink();
        lineLink.userId = CurrentUser.getUid();
        lineLink.url = lineUrl;
        return repository.save(lineLink).subscribeOn(Schedulers.io());
    }
}
