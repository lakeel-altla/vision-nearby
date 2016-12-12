package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ActivityModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.ConfigModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.PresenterModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;
import com.lakeel.altla.vision.nearby.presentation.service.LineService;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation.DistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.SettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.bluetooth.BleSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.cm.CmSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.device.DeviceListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.line.LineSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.tracking.TrackingFragment;

import dagger.Subcomponent;

@InjectScope
@Subcomponent(modules = {ActivityModule.class, PresenterModule.class, RepositoryModule.class, ConfigModule.class})
public interface ViewComponent {

    void inject(MainActivity activity);

    void inject(SignInFragment fragment);

    void inject(FavoriteListFragment fragment);

    void inject(NearbyListFragment fragment);

    void inject(HistoryListFragment fragment);

    void inject(HistoryFragment fragment);

    void inject(SettingFragment fragment);

    void inject(BleSettingFragment fragment);

    void inject(LineSettingFragment fragment);

    void inject(CmSettingFragment fragment);

    void inject(TrackingFragment fragment);

    void inject(DeviceListFragment fragment);

    void inject(DistanceEstimationFragment fragment);

    void inject(FavoriteFragment fragment);

    void inject(LineService service);
}
