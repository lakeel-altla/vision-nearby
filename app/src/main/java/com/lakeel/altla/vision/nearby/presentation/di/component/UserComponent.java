package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.di.ActivityScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ActivityModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.ConfigModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.PresenterModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;
import com.lakeel.altla.vision.nearby.presentation.view.activity.FavoritesUserActivity;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.activity.RecentlyUserUserActivity;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.bluetooth.BleSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.cm.CmSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation.DeviceDistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorites.FavoritesListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.line.LineSettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.recently.RecentlyFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.search.SearchFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.settings.SettingsFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;

import dagger.Subcomponent;

@ActivityScope
@Subcomponent(modules = {ActivityModule.class, PresenterModule.class, RepositoryModule.class, ConfigModule.class})
public interface UserComponent {

    void inject(MainActivity activity);

    void inject(RecentlyUserUserActivity activity);

    void inject(FavoritesUserActivity activity);

    void inject(SignInFragment fragment);

    void inject(FavoritesListFragment fragment);

    void inject(NearbyListFragment fragment);

    void inject(RecentlyFragment fragment);

    void inject(SettingsFragment fragment);

    void inject(BleSettingsFragment fragment);

    void inject(LineSettingsFragment fragment);

    void inject(CmSettingsFragment fragment);

    void inject(SearchFragment fragment);

    void inject(TrackingFragment fragment);

    void inject(DeviceListFragment fragment);

    void inject(DeviceDistanceEstimationFragment fragment);
}