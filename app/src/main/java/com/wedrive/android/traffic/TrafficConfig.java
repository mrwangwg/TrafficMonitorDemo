package com.wedrive.android.traffic;

import com.wedrive.android.traffic.utils.LogUtils;

public class TrafficConfig {

    public static boolean isDebug = true;

    public static long mPeriodTime = 5;


    static {
        if(isDebug){
            boolean logSwitch = true;
            boolean log2FileSwitch = true;
            char logFilter = 'v';
            String tag = "TrafficMonitor";

            LogUtils.Builder builder = new LogUtils.Builder();

            builder.setLogSwitch(logSwitch);
            builder.setLogFilter(logFilter);
            builder.setTag(tag);
            builder.create();
        }
    }
}
