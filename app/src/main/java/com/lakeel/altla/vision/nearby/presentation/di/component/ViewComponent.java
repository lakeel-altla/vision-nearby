package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.application.App;
import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ConfigModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.PresenterModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;
import com.lakeel.altla.vision.nearby.presentation.service.LineService;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bluetooth.BleSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation.DistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.information.InformationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.information.InformationListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.line.LineSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.SettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.user.UserPassingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.user.UserProfileFragment;

import dagger.Subcomponent;

@InjectScope
@Subcomponent(modules = {PresenterModule.class, RepositoryModule.class, ConfigModule.class})
public interface ViewComponent {

    void inject(App app);

    void inject(MainActivity activity);

    void inject(SignInFragment fragment);

    void inject(FavoriteListFragment fragment);

    void inject(NearbyListFragment fragment);

    void inject(HistoryListFragment fragment);

    void inject(UserPassingFragment fragment);

    void inject(InformationListFragment fragment);

    void inject(InformationFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(BleSettingsFragment fragment);

    void inject(LineSettingsFragment fragment);

    void inject(TrackingFragment fragment);

    void inject(DeviceListFragment fragment);

    void inject(DistanceEstimationFragment fragment);

    void inject(UserProfileFragment fragment);

    void inject(LineService service);
}
