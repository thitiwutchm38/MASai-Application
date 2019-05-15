package com.example.bookthiti.masai2.mainscreen.model;

import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

public class PostRequestBody {
    private long testingId;
    private String testingName;
    private Date createdAt;
    private List<ActivityLogEntity> routerCracking;
    private List<ActivityLogEntity> deviceDiscovery;
    private List<ActivityLogEntity> deviceAssessment;
    private List<ActivityLogEntity> portAttack;
    private List<ActivityLogEntity> bluetoothAttack;
    private List<ActivityLogEntity> mobileAppScan;

    public PostRequestBody(long testingId, String testingName, Date createdAt, List<ActivityLogEntity> routerCracking, List<ActivityLogEntity> deviceDiscovery, List<ActivityLogEntity> deviceAssessment, List<ActivityLogEntity> portAttack, List<ActivityLogEntity> bluetoothAttack, List<ActivityLogEntity> mobileAppScan) {
        this.testingId = testingId;
        this.testingName = testingName;
        this.createdAt = createdAt;
        this.routerCracking = routerCracking;
        this.deviceDiscovery = deviceDiscovery;
        this.deviceAssessment = deviceAssessment;
        this.portAttack = portAttack;
        this.bluetoothAttack = bluetoothAttack;
        this.mobileAppScan = mobileAppScan;
    }

    public PostRequestBody() {
    }

    public long getTestingId() {
        return testingId;
    }

    public void setTestingId(long testingId) {
        this.testingId = testingId;
    }

    public String getTestingName() {
        return testingName;
    }

    public void setTestingName(String testingName) {
        this.testingName = testingName;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<ActivityLogEntity> getRouterCracking() {
        return routerCracking;
    }

    public void setRouterCracking(List<ActivityLogEntity> routerCracking) {
        this.routerCracking = routerCracking;
    }

    public List<ActivityLogEntity> getDeviceDiscovery() {
        return deviceDiscovery;
    }

    public void setDeviceDiscovery(List<ActivityLogEntity> deviceDiscovery) {
        this.deviceDiscovery = deviceDiscovery;
    }

    public List<ActivityLogEntity> getDeviceAssessment() {
        return deviceAssessment;
    }

    public void setDeviceAssessment(List<ActivityLogEntity> deviceAssessment) {
        this.deviceAssessment = deviceAssessment;
    }

    public List<ActivityLogEntity> getPortAttack() {
        return portAttack;
    }

    public void setPortAttack(List<ActivityLogEntity> portAttack) {
        this.portAttack = portAttack;
    }

    public List<ActivityLogEntity> getBluetoothAttack() {
        return bluetoothAttack;
    }

    public void setBluetoothAttack(List<ActivityLogEntity> bluetoothAttack) {
        this.bluetoothAttack = bluetoothAttack;
    }

    public List<ActivityLogEntity> getMobileAppScan() {
        return mobileAppScan;
    }

    public void setMobileAppScan(List<ActivityLogEntity> mobileAppScan) {
        this.mobileAppScan = mobileAppScan;
    }

    public static class PostRequestBodySerializer implements JsonSerializer<PostRequestBody> {
        @Override
        public JsonElement serialize(PostRequestBody src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("testingId", new JsonPrimitive(src.testingId));
            jsonObject.addProperty("testingName", src.testingName);
            jsonObject.addProperty("createdAt", src.createdAt.toString());
            Type listOfActivityLogEntity = new TypeToken<List<ActivityLogEntity>>() {
            }.getType();
            JsonElement routerCrackingArray = context.serialize(src.routerCracking, listOfActivityLogEntity);
            JsonElement deviceDiscoveryArray = context.serialize(src.deviceDiscovery, listOfActivityLogEntity);
            JsonElement deviceAssessmentArray = context.serialize(src.deviceAssessment, listOfActivityLogEntity);
            JsonElement portAttackArray = context.serialize(src.portAttack, listOfActivityLogEntity);
            JsonElement bluetoothAttackArray = context.serialize(src.bluetoothAttack, listOfActivityLogEntity);
            JsonElement mobileAppScanArray = context.serialize(src.mobileAppScan, listOfActivityLogEntity);
            jsonObject.add("routerCracking", routerCrackingArray);
            jsonObject.add("deviceDiscovery", deviceDiscoveryArray);
            jsonObject.add("deviceAssessment", deviceAssessmentArray);
            jsonObject.add("portAttack", portAttackArray);
            jsonObject.add("bluetoothAttack", bluetoothAttackArray);
            jsonObject.add("mobileAppScan", mobileAppScanArray);
            return jsonObject;
        }
    }
}
