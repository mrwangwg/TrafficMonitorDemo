package com.wedrive.android.traffic.models;

public class TrafficBean {

    /**
     * 被监听包名
     */
    private String packageName;

    /**
     * 被监听应用的uid
     */
    private int uid;

    /**
     * 起始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 起始--结束时间 上行流量
     */
    private long uploadByte;

    /**
     * 起始--结束时间 下行流量
     */
    private long downloadByte;

    /**
     * 开机-->当前时间 上行流量
     */
    private long totalUploadByte;

    /**
     * 开机-->当前时间 下行流量
     */
    private long totalDownloadByte;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getUploadByte() {
        return uploadByte;
    }

    public void setUploadByte(long uploadByte) {
        this.uploadByte = uploadByte;
    }

    public long getDownloadByte() {
        return downloadByte;
    }

    public void setDownloadByte(long downloadByte) {
        this.downloadByte = downloadByte;
    }

    public long getTotalUploadByte() {
        return totalUploadByte;
    }

    public void setTotalUploadByte(long totalUploadByte) {
        this.totalUploadByte = totalUploadByte;
    }

    public long getTotalDownloadByte() {
        return totalDownloadByte;
    }

    public void setTotalDownloadByte(long totalDownloadByte) {
        this.totalDownloadByte = totalDownloadByte;
    }

    @Override
    public String toString() {
        return "TrafficBean{" +
                "packageName='" + packageName + '\'' +
                ", uid=" + uid +
                ", startTime='" + startTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", uploadByte=" + uploadByte +
                ", downloadByte=" + downloadByte +
                ", totalUploadByte=" + totalUploadByte +
                ", totalDownloadByte=" + totalDownloadByte +
                '}';
    }
}
