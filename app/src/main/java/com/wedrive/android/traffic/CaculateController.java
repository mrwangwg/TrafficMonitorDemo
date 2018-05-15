package com.wedrive.android.traffic;

import com.wedrive.android.traffic.models.TrafficBean;
import com.wedrive.android.traffic.utils.LogUtils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class CaculateController {
    private final String TAG ="CaculateController";

    private SimpleDateFormat mTimeFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");//TODO 移动到工具类
    private static Map<String, CaculateController> mMap = new HashMap<>();

    private boolean isFirst = true;
    private long mUpload, mDownload;
    private long mStartTime;

    public TrafficBean caculate(String packageName, int uid, long time, long upload, long download) {
        LogUtils.i(TAG,"caculate->packageName:"+packageName+", uid:"+uid+", upload:"+upload+", download:"+download);
        TrafficBean trafficBean = null;
        if (isFirst) {
            mStartTime = time;
            mUpload = upload;
            mDownload = download;

            isFirst = false;
        } else {
            long up, down;
            up = upload - mUpload;
            down = download - mDownload;

            LogUtils.e(TAG,"caculate->upload:"+up+" <-->download:"+down);
            if (up != 0 || down != 0) {
                trafficBean = new TrafficBean();
                trafficBean.setPackageName(packageName);
                trafficBean.setUid(uid);
                trafficBean.setTotalUploadByte(upload);
                trafficBean.setTotalDownloadByte(download);
                trafficBean.setStartTime(mTimeFormat.format(mStartTime));
                trafficBean.setEndTime(mTimeFormat.format(time));

                trafficBean.setUploadByte(up);
                trafficBean.setDownloadByte(down);
            }else{
                LogUtils.e(TAG,"caculate->packageName:"+packageName+"  has no changed!");
            }

            mStartTime = time;
            mUpload = upload;
            mDownload = download;
        }
        return trafficBean;
    }

    public void reset() {
        isFirst = true;
        mUpload = 0;
        mDownload = 0;
    }

    public static CaculateController buildCaculateController(String name) {
        CaculateController caculateController = null;
        caculateController = mMap.get(name);
        if (null == caculateController) {
            caculateController = new CaculateController();

            mMap.put(name, new CaculateController());
        }
        return caculateController;
    }

}
