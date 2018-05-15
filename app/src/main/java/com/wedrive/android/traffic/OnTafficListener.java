package com.wedrive.android.traffic;

import com.wedrive.android.traffic.models.TrafficBean;

import java.util.List;

public interface OnTafficListener {

    void onTafficDataChanged(List<TrafficBean> trafficList);



}
