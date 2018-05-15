package com.wedrive.android.traffic;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.util.Log;

import com.wedrive.android.traffic.models.TrafficBean;
import com.wedrive.android.traffic.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TrafficController implements ITrafficController, Runnable {

    private final String TAG = "TrafficController";
    /**
     * 设备上已安装的拥有网络请求权限的PackageInfo
     */
    private List<PackageInfo> mInstalledInternetPackages = new ArrayList<>();
    /**
     * 上层添加的需要监控的应用的包名
     */
    private List<String> mCustomInternetPackages = new ArrayList<>();
    /**
     * TrafficController真实会监听的包含 上层添加的应用(并且已安装的拥有网络请求权限的PackageInfo)
     */
    private List<PackageInfo> mMonitorInternetPackages = new ArrayList<>();

    /**
     * 给上层的回调的获取到的指定时间段的相关应用的流量信息
     */
    private List<TrafficBean> mCallbackList = new ArrayList<>();

    private Context mContext;
    private ScheduledExecutorService service;
    private OnTafficListener mListener;
    private TrafficStatManager mTrafficProxy;

    private final long PERIOD_TIME = 2;
    private long periodTime = PERIOD_TIME;

    public TrafficController(Context context) {
        mContext = context;


        mTrafficProxy = new TrafficStatManager();
        mTrafficProxy.init();


        getInstalledInternetPackages();
    }

    @Override
    public void start(OnTafficListener listener) {
        LogUtils.i(TAG, "start->");

        mListener = listener;
        service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this, 0, periodTime, TimeUnit.SECONDS);
    }

    @Override
    public void setTrafficRate(int seconds) {
        LogUtils.i(TAG, "setTrafficRate->" + seconds);
        periodTime = seconds;
   /*     if (!service.isShutdown()) {
            service.shutdown();

            service.scheduleAtFixedRate(this, 0, periodTime, TimeUnit.SECONDS);
        }*/
    }

    @Override
    public void addMonitorPackage(String packageName) {
        LogUtils.i(TAG, "addMonitorPackage->" + packageName);
        if (checkCustomInternetPackages(packageName)) {
            if (!mCustomInternetPackages.contains(packageName))
                mCustomInternetPackages.add(packageName);
        } else {
            LogUtils.i(TAG, packageName + " add failed!");
        }
    }

    @Override
    public void removeMonitorPackage(String packageName) {
        LogUtils.i(TAG, "removeMonitorPackage->" + packageName);
        if (mMonitorInternetPackages.contains(packageName))
            mMonitorInternetPackages.remove(packageName);
        if (mCustomInternetPackages.contains(packageName))
            mCustomInternetPackages.remove(packageName);
    }

    @Override
    public void clearAllMonitorPackages() {
        mMonitorInternetPackages.clear();
        mCustomInternetPackages.clear();
    }

    @Override
    public void stop() {
        LogUtils.i(TAG, "stop->");
        if (service!=null&&!service.isShutdown()) {
            service.shutdownNow();
        }
    }

    @Override
    public void run() {
        if (!mCallbackList.isEmpty())
            mCallbackList.clear();
        List<PackageInfo> list = null;
        if (mMonitorInternetPackages.size() == 0) {//如果用户没有设置监控的包，就监控所有具有上网权限的应用
            list = mInstalledInternetPackages;
        } else {//监控用户设置的具有上网权限的应用
            list = mMonitorInternetPackages;
        }
        for (PackageInfo info : list) {
            String packageName = info.packageName;

            int uid = info.applicationInfo.uid;

            LogUtils.i(TAG, "uid->" + uid + ";;packageName->" + packageName);

            long[] upDownBytesByUid = mTrafficProxy.getUpDownBytesByUid(uid);
            //upload
            long tx = upDownBytesByUid[0];
            //download
            long rx = upDownBytesByUid[1];

            LogUtils.i(TAG, ";;TX->" + tx + ";;RX->" + rx);

            long l = System.currentTimeMillis();

            TrafficBean caculate = CaculateController.buildCaculateController(packageName).caculate(packageName, uid, l, tx, rx);

            if (null != caculate) {
                mCallbackList.add(caculate);
            }
        }
        if (!mCallbackList.isEmpty())
            mListener.onTafficDataChanged(mCallbackList);
    }

    private List<PackageInfo> getInstalledInternetPackages() {
        if (!mInstalledInternetPackages.isEmpty())
            mInstalledInternetPackages.clear();

        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packinfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
        for (PackageInfo info : packinfos) {
            String[] premissions = info.requestedPermissions;
            if (premissions != null && premissions.length > 0) {
                for (String premission : premissions) {
                    if ("android.permission.INTERNET".equals(premission)) {
                        mInstalledInternetPackages.add(info);
                    }
                }
            }
        }
        return mInstalledInternetPackages;
    }

    private boolean checkCustomInternetPackages(String packageName) {
        for (PackageInfo info : mInstalledInternetPackages) {
            if (info.packageName.equals(packageName)) {
                if (!mMonitorInternetPackages.contains(info.packageName))
                    mMonitorInternetPackages.add(info);
                return true;
            }
        }
        return false;
    }

}
