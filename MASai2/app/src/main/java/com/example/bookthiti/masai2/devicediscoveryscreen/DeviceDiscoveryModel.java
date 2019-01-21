package com.example.bookthiti.masai2.devicediscoveryscreen;

import android.bluetooth.BluetoothClass;

import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceDiscoveryModel {

    private TimeStats timeStats;
    private HostStats hostStats;
    private List<DeviceModel> hosts;

    public DeviceDiscoveryModel() {

    }

    public DeviceDiscoveryModel(TimeStats timeStats, HostStats hostStats, List<DeviceModel> hosts) {
        this.timeStats = timeStats;
        this.hostStats = hostStats;
        this.hosts = hosts;
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

    public static class DeviceDiscoveryModelDeserializer implements JsonDeserializer<DeviceDiscoveryModel> {
        @Override
        public DeviceDiscoveryModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            TimeStats timeStats = context.deserialize(jsonObject.get("timeStats"), TimeStats.class);
            HostStats hostStats = context.deserialize(jsonObject.get("hostStats"), HostStats.class);
            JsonArray jsonArray = jsonObject.get("hosts").getAsJsonArray();
            List<DeviceModel> hosts = new ArrayList<DeviceModel>();
            for (JsonElement jsonElement : jsonArray) {
                JsonObject jsonHost = jsonElement.getAsJsonObject();
                DeviceModel deviceModel = context.deserialize(jsonHost, DeviceModel.class);
                hosts.add(deviceModel);
            }
//            DeviceModel[] deviceModelArray = context.deserialize(jsonObject.get("hosts").getAsJsonArray(), DeviceModel[].class);
//            List<DeviceModel> hosts = Arrays.asList(deviceModelArray);
            return new DeviceDiscoveryModel(timeStats, hostStats, hosts);
        }
    }
}
