package com.wedrive.trafficmonitordemo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wedrive.android.traffic.OnTafficListener;
import com.wedrive.android.traffic.WLTrafficController;
import com.wedrive.android.traffic.models.TrafficBean;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnTafficListener {

    private final String TAG = "MainActivity";

    private static final int UPDATE_LIST_VIEW = 0;
    private static final int UPDATE_LIST_VIEW2 = 1;

    private List<PackageInfo> mInstalledPackages;

    private ListView mListView;
    private EditText timeRate;

    private WLTrafficController mTrafficController;
    private PackageInfoAdpter packageInfoAdpter;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_LIST_VIEW:
                    refreshListView();
                    break;
                case UPDATE_LIST_VIEW2:
                    List<TrafficBean> trafficList = (List<TrafficBean>) msg.obj;
                    refreshListView2(trafficList);
                    break;
                default:
                    break;

            }

            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getTrafficData(android.os.Process.myUid());



//        mTrafficController.setTrafficRate(5);

//        trafficController.addMonitorPackage("com.mapbar.wedrive.launcher");
//        trafficController.addMonitorPackage("com.miui.providers.weather");
//        mTrafficController.addMonitorPackage("com.wedrive.welink.appstore");
//        mTrafficController.addMonitorPackage("com.tencent.qqmusic");
//        mTrafficController.addMonitorPackage(getPackageName());
//        mTrafficController.start(this);

        initView();
        initData();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv);
        timeRate = (EditText) findViewById(R.id.et_time);

    }

    private void initData() {
        mTrafficController = new WLTrafficController(this);

        PackageManager pm = getPackageManager();
        mInstalledPackages = pm.getInstalledPackages(0);

        packageInfoAdpter = new PackageInfoAdpter(this);

        mHandler.sendEmptyMessage(0);
    }

    private void refreshListView() {

        packageInfoAdpter.setPackageInfoList(mInstalledPackages);
        packageInfoAdpter.setTrafficBeanList(null);

        mListView.setAdapter(packageInfoAdpter);
    }

    private void refreshListView2(List<TrafficBean> trafficList) {
        packageInfoAdpter.setPackageInfoList(null);
        packageInfoAdpter.setTrafficBeanList(trafficList);

        mListView.setAdapter(packageInfoAdpter);
    }

    private void clearListView(){
        packageInfoAdpter.setPackageInfoList(null);
        packageInfoAdpter.setTrafficBeanList(null);

        mListView.setAdapter(packageInfoAdpter);
    }

    @Override
    public void onTafficDataChanged(List<TrafficBean> trafficList) {
        String log = "";
        Log.i(TAG, "onTafficDataChanged->start");
        log = "onTafficDataChanged->start";
        for (TrafficBean tb : trafficList) {
            Log.w(TAG, tb.toString());
            log += tb.toString();
        }
        Log.i(TAG, "onTafficDataChanged->end");
        log += "onTafficDataChanged->end";

        Message obtain = Message.obtain();
        obtain.what = UPDATE_LIST_VIEW2;
        obtain.obj = trafficList;
        mHandler.sendMessage(obtain);
    }


    class PackageInfoAdpter extends BaseAdapter {

        private List<TrafficBean> nTrafficList;
        private List<PackageInfo> nInstalledPackages;
        private LayoutInflater mInflater;
        private Context context;

        public PackageInfoAdpter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        public void setTrafficBeanList(List<TrafficBean> trafficList) {
            nTrafficList = trafficList;
        }

        public void setPackageInfoList(List<PackageInfo> installedPackages) {
            nInstalledPackages = installedPackages;
        }

        @Override
        public int getCount() {
            if (null != nInstalledPackages) {
                return nInstalledPackages.size();
            } else if (null != nTrafficList) {
                return nTrafficList.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (null != nInstalledPackages) {
                return nInstalledPackages.get(position);
            } else if (null != nTrafficList) {
                return nTrafficList.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            View view;
            if (convertView == null) {
                holder = new ViewHolder();
                view = mInflater.inflate(R.layout.package_info_item, parent, false);
                holder.tv_package_name = view.findViewById(R.id.tv_package_name);
                holder.tv_package_info = view.findViewById(R.id.tv_package_info);

                holder.bt_add = view.findViewById(R.id.bt_add);
                holder.bt_remove = view.findViewById(R.id.bt_remove);

                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            String packageName = "";
            if (null != nInstalledPackages) {
                packageName = nInstalledPackages.get(position).packageName;
                holder.tv_package_name.setText(packageName);
                holder.tv_package_info.setText(nInstalledPackages.get(position).sharedUserId);

                holder.bt_add.setVisibility(View.VISIBLE);
                holder.bt_remove.setVisibility(View.VISIBLE);

                final String name = packageName;
                holder.bt_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTrafficController.addMonitorPackage(name);

                        Toast.makeText(context, "add->"+name,Toast.LENGTH_LONG).show();
                    }
                });
                holder.bt_remove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTrafficController.removeMonitorPackage(name);
                        Toast.makeText(context, "remove->"+name,Toast.LENGTH_LONG).show();
                    }
                });

            } else if (null != nTrafficList) {
                packageName = nTrafficList.get(position).getPackageName();
                holder.tv_package_name.setText(packageName);
                holder.tv_package_info.setText(nTrafficList.get(position).toString());

                holder.bt_add.setVisibility(View.GONE);
                holder.bt_remove.setVisibility(View.GONE);
            }


            return view;
        }

        class ViewHolder {
            TextView tv_package_name;
            TextView tv_package_info;
            Button bt_add;
            Button bt_remove;
        }

    }


    private void getTrafficData(int uid) {
//        int uid = info.applicationInfo.uid;
/*        long rx = TrafficStats.getUidRxBytes(uid);
        long tx = TrafficStats.getUidTxBytes(uid);*/


        /** 获取手机通过 2G/3G 接收的字节流量总数 */
        Log.e(TAG, "getMobileRxBytes->" + TrafficStats.getMobileRxBytes());

        /** 获取手机通过 2G/3G 接收的数据包总数 */
        TrafficStats.getMobileRxPackets();
        Log.e(TAG, "getMobileRxPackets->" + TrafficStats.getMobileRxPackets());

        /** 获取手机通过 2G/3G 发出的字节流量总数 */
        TrafficStats.getMobileTxBytes();
        Log.e(TAG, "getMobileTxBytes->" + TrafficStats.getMobileTxBytes());

        /** 获取手机通过 2G/3G 发出的数据包总数 */
        TrafficStats.getMobileTxPackets();
        Log.e(TAG, "getMobileTxPackets->" + TrafficStats.getMobileTxPackets());

        /** 获取手机通过所有网络方式接收的字节流量总数(包括 wifi) */
        TrafficStats.getTotalRxBytes();
        Log.e(TAG, "getTotalRxBytes->" + TrafficStats.getTotalRxBytes());

        /** 获取手机通过所有网络方式接收的数据包总数(包括 wifi) */
        TrafficStats.getTotalRxPackets();
        Log.e(TAG, "getTotalRxPackets->" + TrafficStats.getTotalRxPackets());

        /** 获取手机通过所有网络方式发送的字节流量总数(包括 wifi) */
        TrafficStats.getTotalTxBytes();
        Log.e(TAG, "getTotalTxBytes()->" + TrafficStats.getTotalTxBytes());

        /** 获取手机通过所有网络方式发送的数据包总数(包括 wifi) */
        TrafficStats.getTotalTxPackets();
        Log.e(TAG, "getTotalTxPackets->" + TrafficStats.getTotalTxPackets());

        /** 获取手机指定 UID 对应的应程序用通过所有网络方式接收的字节流量总数(包括 wifi) */
//        TrafficStats.getUidRxBytes(uid);
        Log.e(TAG, "getUidRxBytes->" + TrafficStats.getUidRxBytes(uid));

        /** 获取手机指定 UID 对应的应用程序通过所有网络方式发送的字节流量总数(包括 wifi) */
//        TrafficStats.getUidTxBytes(uid);
        Log.e(TAG, "getUidTxBytes->" + TrafficStats.getUidTxBytes(uid));
    }


    public void start(View v){
        Editable text = timeRate.getText();
        Integer integer = Integer.valueOf(text.toString());

        mTrafficController.setTrafficRate(integer);
        mTrafficController.start(this);

        clearListView();
    }

    public void stop(View v){
        mTrafficController.stop();
        mTrafficController.clearAllMonitorPackages();

        mHandler.sendEmptyMessage(0);
    }

    public void test(View v) {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.baidu.com");

                    URLConnection rulConnection = null;// 此处的urlConnection对象实际上是根据URL的

                    rulConnection = url.openConnection();

                    // 请求协议(此处是http)生成的URLConnection类
                    // 的子类HttpURLConnection,故此处最好将其转化
                    // 为HttpURLConnection类型的对象,以便用到
                    // HttpURLConnection更多的API.如下:

                    HttpURLConnection httpUrlConnection = (HttpURLConnection) rulConnection;

                    // 设置是否向httpUrlConnection输出，因为这个是post请求，参数要放在
                    // http正文内，因此需要设为true, 默认情况下是false;
                    httpUrlConnection.setDoOutput(true);

                    // 设置是否从httpUrlConnection读入，默认情况下是true;
                    httpUrlConnection.setDoInput(true);

                    // Post 请求不能使用缓存
                    httpUrlConnection.setUseCaches(false);

                    // 设定传送的内容类型是可序列化的java对象
                    // (如果不设此项,在传送序列化对象时,当WEB服务默认的不是这种类型时可能抛java.io.EOFException)
                    httpUrlConnection.setRequestProperty("Content-type", "application/x-java-serialized-object");

                    // 设定请求的方法为"POST"，默认是GET
                    httpUrlConnection.setRequestMethod("POST");

                    // 连接，从上述第2条中url.openConnection()至此的配置必须要在connect之前完成，
                    httpUrlConnection.connect();

                    getTrafficData(android.os.Process.myUid());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                super.run();
            }
        }.start();
    }
}
