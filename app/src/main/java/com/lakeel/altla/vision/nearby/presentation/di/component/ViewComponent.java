package com.lakeel.altla.vision.nearby.presentation.di.component;

import com.lakeel.altla.vision.nearby.presentation.di.InjectScope;
import com.lakeel.altla.vision.nearby.presentation.di.module.ActivityModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.ConfigModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.PresenterModule;
import com.lakeel.altla.vision.nearby.presentation.di.module.RepositoryModule;
import com.lakeel.altla.vision.nearby.presentation.service.LineService;
import com.lakeel.altla.vision.nearby.presentation.view.activity.MainActivity;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.estimation.DistanceEstimationFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.user.UserProfileFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.favorite.FavoriteListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.user.UserPassingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.history.HistoryFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.setting.SettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.bluetooth.BleSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.cm.CmSettingFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.device.DeviceListListFragment;
import com.lakeel.altla.vision.nearby.presentation.view.fragment.line.LineSettingFragment;
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

    void inject(HistoryFragment fragment);

    void inject(UserPassingFragment fragment);

    void inject(SettingFragment fragment);

    void inject(BleSettingFragment fragment);

    void inject(LineSettingFragment fragment);

    void inject(CmSettingFragment fragment);

    void inject(TrackingFragment fragment);

    void inject(DeviceListListFragment fragment);

    void inject(DistanceEstimationFragment fragment);

    void inject(UserProfileFragment fragment);

    void inject(LineService service);
}
