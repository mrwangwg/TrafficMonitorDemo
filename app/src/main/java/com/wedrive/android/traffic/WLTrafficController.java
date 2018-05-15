package com.wedrive.android.traffic;

import android.content.Context;

public final class WLTrafficController extends TrafficController{

    public WLTrafficController(Context context) {
        super(context);
    }

    @Override
    public void start(OnTafficListener listener) {
        super.start(listener);
    }

    @Override
    public void setTrafficRate(int seconds) {
        super.setTrafficRate(seconds);
    }

    @Override
    public void addMonitorPackage(String packageName) {
        super.addMonitorPackage(packageName);
    }

    @Override
    public void removeMonitorPackage(String packageName) {
        super.removeMonitorPackage(packageName);
    }

    @Override
    public void clearAllMonitorPackages() {
        super.clearAllMonitorPackages();
    }

    @Override
    public void stop() {
        super.stop();
    }
}
