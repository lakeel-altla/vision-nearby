package com.lakeel.altla.vision.nearby.presentation.analytics;

import android.content.Context;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.lakeel.altla.vision.nearby.presentation.ble.BleChecker;

public final class AnalyticsReporter {

    private final FirebaseAnalytics firebaseAnalytics;

    public AnalyticsReporter(Context context) {
        firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    public void login() {
        UserParam userParam = new UserParam();
        logEvent(FirebaseAnalytics.Event.LOGIN, userParam);
    }

    public void logout() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.LOG_OUT, userParam);
    }

    public void viewFavoriteItem(String userId, String userName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.FAVORITE_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.FAVORITE_USER_NAME.getValue(), userName);
        logEvent(AnalyticsEvent.VIEW_FAVORITE_ITEM, userParam);
    }

    public void addFavorite(String userId, String userName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.FAVORITE_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.FAVORITE_USER_NAME.getValue(), userName);
        logEvent(AnalyticsEvent.ADD_FAVORITE, userParam);
    }

    public void removeFavorite(String userId, String userName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.FAVORITE_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.FAVORITE_USER_NAME.getValue(), userName);
        logEvent(AnalyticsEvent.REMOVE_FAVORITE, userParam);
    }

    public void foundDevice(String deviceId, String deviceName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.DEVICE_ID.getValue(), deviceId);
        userParam.putString(AnalyticsParam.DEVICE_NAME.getValue(), deviceName);
        logEvent(AnalyticsEvent.FOUND_DEVICE, userParam);
    }

    public void lostDevice(String deviceId, String deviceName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.DEVICE_ID.getValue(), deviceId);
        userParam.putString(AnalyticsParam.DEVICE_NAME.getValue(), deviceName);
        logEvent(AnalyticsEvent.LOST_DEVICE, userParam);
    }

    public void removeDevice(String deviceId, String deviceName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.DEVICE_ID.getValue(), deviceId);
        userParam.putString(AnalyticsParam.DEVICE_NAME.getValue(), deviceName);
        logEvent(AnalyticsEvent.REMOVE_DEVICE, userParam);
    }

    public void estimateDistance(String targetName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.TARGET_NAME.getValue(), targetName);
        logEvent(AnalyticsEvent.ESTIMATE_DISTANCE, userParam);
    }

    public void launchGoogleMap() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.LAUNCH_GOOGLE_MAP, userParam);
    }

    public void viewHistoryItem(String userId, String userName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.HISTORY_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.HISTORY_USER_NAME.getValue(), userName);
        logEvent(AnalyticsEvent.VIEW_HISTORY_ITEM, userParam);
    }

    public void addHistory(String userId, String userName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.HISTORY_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.HISTORY_USER_NAME.getValue(), userName);
        logEvent(AnalyticsEvent.ADD_HISTORY, userParam);
    }

    public void removeHistory(String userId, String userName) {
        UserParam userParam = new UserParam();
        userParam.putString(AnalyticsParam.HISTORY_USER_ID.getValue(), userId);
        userParam.putString(AnalyticsParam.HISTORY_USER_NAME.getValue(), userName);
        logEvent(AnalyticsEvent.REMOVE_HISTORY, userParam);
    }

    public void inputLineUrl() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.INPUT_LINE_URL, userParam);
    }

    public void onAdvertise() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.ON_ADVERTISE, userParam);
    }

    public void offAdvertise() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.OFF_ADVERTISE, userParam);
    }

    public void onSubscribe() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.ON_SUBSCRIBE, userParam);
    }

    public void offSubscribe() {
        UserParam userParam = new UserParam();
        logEvent(AnalyticsEvent.OFF_SUBSCRIBE, userParam);
    }

    public void setBleProperty(BleChecker.State state) {
        setUserProperty(AnalyticsProperty.BLE_STATE, state.getValue());
    }

    private void logEvent(AnalyticsEvent event, UserParam param) {
        firebaseAnalytics.logEvent(event.getValue(), param.toBundle());
    }

    private void logEvent(String event, UserParam param) {
        firebaseAnalytics.logEvent(event, param.toBundle());
    }

    private void setUserProperty(AnalyticsProperty property, String value) {
        firebaseAnalytics.setUserProperty(property.getValue(), value);
    }
}
