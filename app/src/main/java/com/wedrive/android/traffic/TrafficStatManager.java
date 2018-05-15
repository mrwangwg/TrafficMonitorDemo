package com.wedrive.android.traffic;

import com.wedrive.android.traffic.utils.LogUtils;

import java.io.File;

public class TrafficStatManager {

    private final String TAG ="TrafficStatManager";

    private static final String DEFAULT_UID_PATH = "/proc/uid_stat/";
    private static final String DEFAULT_UID_PATH2 = "/proc/net/xt_qtaguid/stats";
    private ITrafficModel mTrafficProxy;

    public void init() {
//        mTrafficProxy = new TrafficUidStatProxy();

        File file1 = new File(DEFAULT_UID_PATH );
        File file2 = new File(DEFAULT_UID_PATH2);

        if (android.os.Process.myUid() == android.os.Process.SYSTEM_UID) {
            LogUtils.e(TAG, "step->TrafficStatProy");
            mTrafficProxy = new TrafficStatProy();
        } else if (file1.exists() && file1.canRead()) {
            LogUtils.e(TAG, "step->TrafficUidStatProxy");
            mTrafficProxy = new TrafficUidStatProxy();
        } else if (file2.exists() && file2.canRead()) {
            LogUtils.e(TAG, "step->TrafficNetStatProxy");
            mTrafficProxy = new TrafficNetStatsProxy();
        } else {
            LogUtils.e(TAG, "step->null");
        }
    }

    public long getUploadBytesByUid(int uid) {
        return mTrafficProxy.getUploadBytesByUid(uid);
    }

    public long getDownloadBytesByUid(int uid) {
        return mTrafficProxy.getDownloadBytesByUid(uid);
    }

    public long[] getUpDownBytesByUid(int uid) {
        return mTrafficProxy.getUpDownBytesByUid(uid);
    }
}
