package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.PresenterModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;
import com.lakeel.altla.vision.nearby.presentation.service.LINEService;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.ble.BleSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.distance.DistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteUserFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.NearbyHistoryListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.information.InformationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.information.InformationListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.line.LineSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyUserListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.passing.PassingUserFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.SettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;

import dagger.Subcomponent;

@InjectScope
@Subcomponent(modules = {PresenterModule.class, RepositoryModule.class})
public interface ViewComponent {

    void inject(MainActivity activity);

    void inject(SignInFragment fragment);

    void inject(FavoriteListFragment fragment);

    void inject(NearbyUserListFragment fragment);

    void inject(NearbyHistoryListFragment fragment);

    void inject(PassingUserFragment fragment);

    void inject(InformationListFragment fragment);

    void inject(InformationFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(BleSettingsFragment fragment);

    void inject(LineSettingsFragment fragment);

    void inject(TrackingFragment fragment);

    void inject(DeviceListFragment fragment);

    void inject(DistanceEstimationFragment fragment);

    void inject(FavoriteUserFragment fragment);

    void inject(LINEService service);
}
