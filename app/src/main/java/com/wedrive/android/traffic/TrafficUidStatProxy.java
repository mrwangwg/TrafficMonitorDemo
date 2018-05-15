package com.wedrive.android.traffic;

import com.wedrive.android.traffic.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class TrafficUidStatProxy implements ITrafficModel {

    private final String TAG = "TrafficUidStatProxy";
    // "/proc/uid_stat/{uid}/tcp_snd"
// "/proc/uid_stat/{uid}/tcp_rcv"

    private static final String DEFAULT_UID_PATH = "/proc/uid_stat/";
    private long mUploadBytes, mDownloadBytes;

    @Override
    public long getUploadBytesByUid(int uid) {
        getTotalBytesManual(uid);
        return mUploadBytes;
    }

    @Override
    public long getDownloadBytesByUid(int uid) {
        getTotalBytesManual(uid);
        return mDownloadBytes;
    }

    @Override
    public long[] getUpDownBytesByUid(int uid) {
        getTotalBytesManual(uid);
        return new long[]{mUploadBytes, mDownloadBytes};
    }

    /**
     * 通过uid查询文件夹中的数据
     *
     * @param localUid
     * @return
     */
    private long getTotalBytesManual(int localUid) {
        LogUtils.i(TAG, "getTotalBytesManual->localUid:" + localUid);
        File dir = new File(DEFAULT_UID_PATH);
        String[] children = dir.list();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < children.length; i++) {
            stringBuffer.append(children[i]);
            stringBuffer.append("   ");
        }
        if (!Arrays.asList(children).contains(String.valueOf(localUid))) {
            mUploadBytes = mDownloadBytes = 0L;

            LogUtils.e(TAG, children.length + "");
            LogUtils.e(TAG, stringBuffer.toString());
            LogUtils.e(TAG, "localUid:" + localUid+" is not exists!");
            return 0L;
        }
        File uidFileDir = new File(DEFAULT_UID_PATH + String.valueOf(localUid));
        File uidActualFileReceived = new File(uidFileDir, "tcp_rcv");
        File uidActualFileSent = new File(uidFileDir, "tcp_snd");
        String textReceived = "0";
        String textSent = "0";
        try {
            BufferedReader brReceived = new BufferedReader(new FileReader(uidActualFileReceived));
            BufferedReader brSent = new BufferedReader(new FileReader(uidActualFileSent));
            String receivedLine;
            String sentLine;

            if ((receivedLine = brReceived.readLine()) != null) {
                textReceived = receivedLine;
                brReceived.close();
                LogUtils.i(TAG, "receivedLine:" + receivedLine);
            }
            if ((sentLine = brSent.readLine()) != null) {
                textSent = sentLine;
                brSent.close();
                LogUtils.i(TAG, "sentLine:" + sentLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
            LogUtils.e(TAG, e.toString());
        }
        LogUtils.i(TAG, "localUid:" + localUid);

        mUploadBytes = Long.valueOf(textSent).longValue();
        mDownloadBytes = Long.valueOf(textReceived).longValue();

        LogUtils.e(TAG, "mUploadBytes:" + mUploadBytes+"-->mDownloadBytes:"+mDownloadBytes);
        return Long.valueOf(textReceived).longValue() + Long.valueOf(textSent).longValue();
    }


}
