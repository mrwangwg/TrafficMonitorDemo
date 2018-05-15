package com.wedrive.android.traffic;

public interface ITrafficController {

    void start(OnTafficListener listener);

    void setTrafficRate(int seconds);

    void addMonitorPackage(String packageName);

    void removeMonitorPackage(String packageName);

    void clearAllMonitorPackages();

    void stop();

}
