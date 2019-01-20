package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import android.app.Service;

import com.example.bookthiti.masai2.R;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class DeviceModel {

    @SerializedName("status")
    private String status;

    @SerializedName("ipv4")
    private String ipAddress;

    @SerializedName("mac")
    private String macAddress;

    @SerializedName("deviceType")
    private String deviceType;

    @SerializedName("osName")
    private String osName;

    @SerializedName("osVendor")
    private String osVendor;

    @SerializedName("osCpe")
    private String osCpe;

    @SerializedName("services")
    private List<ServiceModel> serviceModels;

    private transient int iconId;

    public DeviceModel() {

    }

    public DeviceModel(String status, String ipAddress, String macAddress, String deviceType, String osName, String osVendor, String osCpe, List<ServiceModel> serviceModels) {
        this.status = status;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.deviceType = deviceType;
        this.osName = osName;
        this.osVendor = osVendor;
        this.osCpe = osCpe;
        this.serviceModels = serviceModels;
        if (this.deviceType != null) {
            switch (this.deviceType) {
                case "phone":
                    iconId = R.drawable.icons_phone;
                    break;
                case "printer":
                    iconId = R.drawable.icons_printer;
                    break;
                case "router":
                    iconId = R.drawable.icons_router;
                    break;
                case "webcam":
                    iconId = R.drawable.icons_cam;
                    break;
                case "media device":
                    iconId = R.drawable.icons_media;
                    break;
                case "general purpose":
                default:
                    iconId = R.drawable.icons_general;
            }
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVendor() {
        return osVendor;
    }

    public void setOsVendor(String osVendor) {
        this.osVendor = osVendor;
    }

    public String getOsCpe() {
        return osCpe;
    }

    public void setOsCpe(String osCpe) {
        this.osCpe = osCpe;
    }

    public List<ServiceModel> getServiceModels() {
        return serviceModels;
    }

    public void setServiceModels(List<ServiceModel> serviceModels) {
        this.serviceModels = serviceModels;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public static class DeviceModelDeserializer implements JsonDeserializer<DeviceModel> {
        @Override
        public DeviceModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String status = jsonObject.get("status").getAsString();
            String ipAddress = jsonObject.get("ipv4").getAsString();
            String macAddress = jsonObject.get("mac") != null ? jsonObject.get("mac").getAsString() : null;
            String deviceType = jsonObject.get("deviceType") != null ? jsonObject.get("deviceType").getAsString() : null;
            String osName = jsonObject.get("osName") != null ? jsonObject.get("osName").getAsString() : null;
            String osCpe = jsonObject.get("osCpe") != null ? jsonObject.get("osCpe").getAsString() : null;
            String osVendor = jsonObject.get("osVendor") != null ? jsonObject.get("osVendor").getAsString() : null;
            ServiceModel[] serviceModelArray = context.deserialize(jsonObject.get("services"), ServiceModel[].class);
            List<ServiceModel> serviceModels = Arrays.asList(serviceModelArray);
            return new DeviceModel(status, ipAddress, macAddress, deviceType, osName, osVendor, osCpe, serviceModels);
        }
    }


}
