package com.lakeel.profile.notification.presentation.di.component;

import com.lakeel.profile.notification.presentation.di.ActivityScope;
import com.lakeel.profile.notification.presentation.di.module.ActivityModule;
import com.lakeel.profile.notification.presentation.di.module.ConfigModule;
import com.lakeel.profile.notification.presentation.di.module.PresenterModule;
import com.lakeel.profile.notification.presentation.di.module.RepositoryModule;
import com.lakeel.profile.notification.presentation.view.activity.FavoritesUserActivity;
import com.lakeel.profile.notification.presentation.view.activity.MainActivity;
import com.lakeel.profile.notification.presentation.view.activity.RecentlyUserUserActivity;
import com.lakeel.profile.notification.presentation.view.fragment.favorites.FavoritesListFragment;
import com.lakeel.profile.notification.presentation.view.fragment.nearby.NearbyListFragment;
import com.lakeel.profile.notification.presentation.view.fragment.recently.RecentlyFragment;
import com.lakeel.profile.notification.presentation.view.fragment.search.SearchFragment;
import com.lakeel.profile.notification.presentation.view.fragment.settings.SettingsFragment;
import com.lakeel.profile.notification.presentation.view.fragment.signin.SignInFragment;
import com.lakeel.profile.notification.presentation.view.fragment.tracking.TrackingFragment;

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

    void inject(SearchFragment fragment);

    void inject(TrackingFragment fragment);
}
