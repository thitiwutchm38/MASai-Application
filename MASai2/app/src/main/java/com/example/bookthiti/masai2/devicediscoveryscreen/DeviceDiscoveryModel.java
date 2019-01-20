package com.example.bookthiti.masai2.devicediscoveryscreen;

import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class DeviceDiscoveryModel {

    private TimeStats timeStats;
    private HostStats hostStats;
    private List<DeviceModel> hosts;

    public DeviceDiscoveryModel() {

    }

    public List<DeviceModel> getHosts() {
        return hosts;
    }

    public void setHosts(List<DeviceModel> hosts) {
        this.hosts = hosts;
    }

    public TimeStats getTimeStats() {
        return timeStats;
    }

    public void setTimeStats(TimeStats timeStats) {
        this.timeStats = timeStats;
    }

    public void setTimeStats(JsonObject jsonTimeStats) {
        String startTime = jsonTimeStats.get("startTime").getAsString();
        String finishTime = jsonTimeStats.get("finishTime").getAsString();
    }

    public HostStats getHostStats() {
        return hostStats;
    }

    public void setHostStats(HostStats hostStats) {
        this.hostStats = hostStats;
    }

    public void setHostStats(JsonObject jsonHostStats) {

    }

    public class TimeStats {
        private String startTime;
        private String finishTime;
        private float elapsed;

        public TimeStats(String startTime, String finishTime, float elapsed) {
            this.startTime = startTime;
            this.finishTime = finishTime;
            this.elapsed = elapsed;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public float getElapsed() {
            return elapsed;
        }

        public void setElapsed(float elapsed) {
            this.elapsed = elapsed;
        }
    }

    public class HostStats {
        private int up;
        private int down;
        private int total;

        public HostStats(int up, int down, int total) {
            this.up = up;
            this.down = down;
            this.total = total;
        }

        public int getUp() {
            return up;
        }

        public void setUp(int up) {
            this.up = up;
        }

        public int getDown() {
            return down;
        }

        public void setDown(int down) {
            this.down = down;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
