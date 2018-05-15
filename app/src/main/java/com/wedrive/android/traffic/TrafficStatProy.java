package com.wedrive.android.traffic;

import android.net.TrafficStats;
/*    final int callingUid = android.os.Process.myUid();
        if (callingUid == android.os.Process.SYSTEM_UID || callingUid == uid) {
        return nativeGetUidStat(uid, TYPE_TX_BYTES);
    } else {
        return UNSUPPORTED;
    }*/
/**
 * 现在根据uid获取上下行流量的方法，只有系统uid 或 获取自身才会生效
 */
public class TrafficStatProy implements ITrafficModel {

    @Override
    public long getUploadBytesByUid(int uid) {
        return TrafficStats.getUidTxBytes(uid);
    }

    @Override
    public long getDownloadBytesByUid(int uid) {
        return TrafficStats.getUidRxBytes(uid);
    }

    @Override
    public long[] getUpDownBytesByUid(int uid) {
        return new long[]{TrafficStats.getUidTxBytes(uid), TrafficStats.getUidRxBytes(uid)};
    }

    private void getTrafficData(int uid) {
//        int uid = info.applicationInfo.uid;
        long rx = TrafficStats.getUidRxBytes(uid);
        long tx = TrafficStats.getUidTxBytes(uid);


        /** 获取手机通过 2G/3G 接收的字节流量总数 */
        TrafficStats.getMobileRxBytes();
        /** 获取手机通过 2G/3G 接收的数据包总数 */
        TrafficStats.getMobileRxPackets();
        /** 获取手机通过 2G/3G 发出的字节流量总数 */
        TrafficStats.getMobileTxBytes();
        /** 获取手机通过 2G/3G 发出的数据包总数 */
        TrafficStats.getMobileTxPackets();
        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
        TrafficStats.getTotalRxBytes();
        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
        TrafficStats.getTotalRxPackets();
        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
        TrafficStats.getTotalTxBytes();
        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
        TrafficStats.getTotalTxPackets();
        /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
        TrafficStats.getUidRxBytes(uid);
        /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
        TrafficStats.getUidTxBytes(uid);
    }
}
