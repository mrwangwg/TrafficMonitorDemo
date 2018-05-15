package com.wedrive.android.traffic;

public interface ITrafficModel {

    /**
     * 获取指定应用的上行流量
     * @param uid
     * @return
     */
    long getUploadBytesByUid(int uid);

    /**
     * 获取指定应用的下行流量
     * @param uid
     * @return
     */
    long getDownloadBytesByUid(int uid);


    /**
     * 获取上下行流量 作一个接口返回
     * @param uid
     * @return
     */
    long[] getUpDownBytesByUid(int uid);

}
