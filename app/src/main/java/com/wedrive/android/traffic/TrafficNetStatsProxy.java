package com.wedrive.android.traffic;

import com.wedrive.android.traffic.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class TrafficNetStatsProxy implements ITrafficModel {

    private final String TAG = "TrafficNetStatsProxy";

    private static final String DEFAULT_NET_UID_PATH = "/proc/net/xt_qtaguid/stats";
    private long mUploadBytes, mDownloadBytes;

    @Override
    public long getUploadBytesByUid(int uid) {
        readUpDownBytesByUid(uid);
        return mUploadBytes;
    }

    @Override
    public long getDownloadBytesByUid(int uid) {
        readUpDownBytesByUid(uid);
        return mDownloadBytes;
    }

    @Override
    public long[] getUpDownBytesByUid(int uid) {
        readUpDownBytesByUid(uid);

        return new long[]{mUploadBytes, mDownloadBytes};
    }

    //stats文件的第4、6(rx)、8(tx)位
    private void readUpDownBytesByUid(int uid) {
        LogUtils.i(TAG, "getTotalBytesManual->localUid:" + uid);
        File fin = new File(DEFAULT_NET_UID_PATH);
        try {
            FileInputStream fis = new FileInputStream(fin);

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line = null;
            while ((line = br.readLine()) != null) {
                LogUtils.i(TAG, "line->" + line);
                String[] split = line.split(" ");
                LogUtils.i(TAG, "split.length->" + split.length);
                LogUtils.i(TAG, "split[3]->" + split[3]);
                if ("uid_tag_int".equals(split[3])) {
                    continue;
                } else if (uid == Integer.valueOf(split[3])) {
                    mDownloadBytes = Long.valueOf(split[5]);
                    mUploadBytes = Long.valueOf(split[7]);
                    break;
                }
            }
            LogUtils.i(TAG, "line->end->" + line);
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogUtils.i(TAG, "FileNotFoundException:" + e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.i(TAG, "IOException:" + e.toString());
        }

    }
}
