package com.example.bookthiti.masai2.deviceassessmentscreen;

import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeviceAssessmentResult {

    private DeviceModel deviceModel;
    private List<String> insecureServices;
    private List<String> secureServices;

    public DeviceAssessmentResult(DeviceModel deviceModel, List<String> insecureServices, List<String> secureServices) {
        this.deviceModel = deviceModel;
        this.insecureServices = insecureServices;
        this.secureServices = secureServices;
    }

    public DeviceAssessmentResult() {
    }

    public DeviceModel getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(DeviceModel deviceModel) {
        this.deviceModel = deviceModel;
    }

    public List<String> getInsecureServices() {
        return insecureServices;
    }

    public void setInsecureServices(List<String> insecureServices) {
        this.insecureServices = insecureServices;
    }

    public List<String> getSecureServices() {
        return secureServices;
    }

    public void setSecureServices(List<String> secureServices) {
        this.secureServices = secureServices;
    }

    public static class DeviceAssessmentResultDeserializer implements JsonDeserializer<DeviceAssessmentResult> {

        @Override
        public DeviceAssessmentResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            DeviceModel deviceModel = null;
            if (jsonObject.get("host") != null && !jsonObject.get("host").isJsonNull()) {
                deviceModel = context.deserialize(jsonObject.get("host"), DeviceModel.class);
            }
            String[] insecureServiceArray = {};
            if (jsonObject.get("insecureServices") != null && !jsonObject.get("insecureServices").isJsonNull()) {
                insecureServiceArray = context.deserialize(jsonObject.get("insecureServices"), String[].class);
            }
            List<String> insecureServices = Arrays.asList(insecureServiceArray);
            String[] secureServiceArray = {};
            if (jsonObject.get("secureServices") != null && !jsonObject.get("secureServices").isJsonNull()) {
                secureServiceArray = context.deserialize(jsonObject.get("secureServices"), String[].class);
            }
            List<String> secureServices = Arrays.asList(secureServiceArray);
            return new DeviceAssessmentResult(deviceModel, insecureServices, secureServices);
        }
    }

}
