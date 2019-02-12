package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TargetApplicationScanningResult {

    private List<AppVulnerability> appVulnerabilityList;
    private List<Permission> permissionList;
    private String packageName;
    private int versionCode;
    private String status;

    public TargetApplicationScanningResult() {
    }

    public TargetApplicationScanningResult(List<AppVulnerability> appVulnerabilityList, List<Permission> permissionList, String packageName, int versionCode, String status) {
        this.appVulnerabilityList = appVulnerabilityList;
        this.permissionList = permissionList;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.status = status;
    }

    public List<AppVulnerability> getAppVulnerabilityList() {
        return appVulnerabilityList;
    }

    public void setAppVulnerabilityList(List<AppVulnerability> appVulnerabilityList) {
        this.appVulnerabilityList = appVulnerabilityList;
    }

    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class TargetApplicationScanningResultDeserializer implements JsonDeserializer<TargetApplicationScanningResult> {

        @Override
        public TargetApplicationScanningResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String packageName = jsonObject.get("packageName") != null && !jsonObject.get("packageName").isJsonNull() ? jsonObject.get("packageName").getAsString() : null;
            int versionCode = jsonObject.get("versionCode") != null && !jsonObject.get("versionCode").isJsonNull() ? jsonObject.get("versionCode").getAsInt() : null;
            String status = jsonObject.get("status") != null && !jsonObject.get("status").isJsonNull() ? jsonObject.get("status").getAsString() : null;
            List<Permission> permissionList = new ArrayList<Permission>();
            if (jsonObject.get("permissions") != null && !jsonObject.get("permissions").isJsonNull()) {
                Permission[] permissions = context.deserialize(jsonObject.get("permissions"), Permission[].class);
                permissionList = Arrays.asList(permissions);
            }
            List<AppVulnerability> appVulnerabilityList = new ArrayList<AppVulnerability>();
            if (jsonObject.get("findings") != null && !jsonObject.get("findings").isJsonNull()) {
                AppVulnerability[] findings = context.deserialize(jsonObject.get("findings"), AppVulnerability[].class);
                appVulnerabilityList = Arrays.asList(findings);
            }

            return new TargetApplicationScanningResult(appVulnerabilityList, permissionList, packageName, versionCode, status);
        }
    }
}
