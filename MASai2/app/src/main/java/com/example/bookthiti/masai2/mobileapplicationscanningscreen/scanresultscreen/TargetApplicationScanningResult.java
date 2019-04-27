package com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class TargetApplicationScanningResult {

    private List<AppVulnerability> appVulnerabilityList;
    private List<Permission> permissionList;
    private String packageName;
    private int versionCode;
    private String versionString;
    private double averageCvss;
    private JsonObject appDetails;
    private String status;
    private JsonObject jsonObject;

    public TargetApplicationScanningResult() {
    }

    public TargetApplicationScanningResult(List<AppVulnerability> appVulnerabilityList, List<Permission> permissionList, String packageName, int versionCode, String versionString, double averageCvss, JsonObject appDetails, String status) {
        this.appVulnerabilityList = appVulnerabilityList;
        this.permissionList = permissionList;
        this.packageName = packageName;
        this.versionCode = versionCode;
        this.versionString = versionString;
        this.averageCvss = averageCvss;
        this.appDetails = appDetails;
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

    public String getVersionString() {
        return versionString;
    }

    public void setVersionString(String versionString) {
        this.versionString = versionString;
    }

    public double getAverageCvss() {
        return averageCvss;
    }

    public void setAverageCvss(double averageCvss) {
        this.averageCvss = averageCvss;
    }

    public JsonObject getAppDetails() {
        return appDetails;
    }

    public void setAppDetails(JsonObject appDetails) {
        this.appDetails = appDetails;
    }

    public JsonObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public static class TargetApplicationScanningResultDeserializer implements JsonDeserializer<TargetApplicationScanningResult> {

        @Override
        public TargetApplicationScanningResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            Log.i(TAG_INFO, "jsonObject is not null? " + (jsonObject != null));
            String packageName = jsonObject.get("packageName") != null && !jsonObject.get("packageName").isJsonNull() ? jsonObject.get("packageName").getAsString() : null;
            int versionCode = jsonObject.get("versionCode") != null && !jsonObject.get("versionCode").isJsonNull() ? jsonObject.get("versionCode").getAsInt() : null;
            String versionString = jsonObject.get("versionString") != null && !jsonObject.get("versionString").isJsonNull() ? jsonObject.get("versionString").getAsString() : null;
            double averageCvss = jsonObject.get("averageCvss") != null && !jsonObject.get("averageCvss").isJsonNull() ? jsonObject.get("averageCvss").getAsDouble() : null;
            Log.i(TAG_INFO, "averageCvss: " + averageCvss);
            JsonObject appDetails = jsonObject.get("appNormalDetail") != null && !jsonObject.get("appNormalDetail").isJsonNull() ? jsonObject.get("appNormalDetail").getAsJsonObject() : null;
            Log.i(TAG_INFO, "appDetails is not null? " + (appDetails != null));
            String status = jsonObject.get("status") != null && !jsonObject.get("status").isJsonNull() ? jsonObject.get("status").getAsString() : null;
            List<Permission> permissionList = new ArrayList<Permission>();
            if (jsonObject.get("permissions") != null && !jsonObject.get("permissions").isJsonNull()) {
                Permission[] permissions = context.deserialize(jsonObject.get("permissions"), Permission[].class);
                permissionList = Arrays.asList(permissions);
            }
            List<AppVulnerability> appVulnerabilityList = new ArrayList<AppVulnerability>();
            if (jsonObject.get("findings") != null && !jsonObject.get("findings").isJsonNull()) {
                AppVulnerability[] findings = context.deserialize(jsonObject.get("findings"), AppVulnerability[].class);
                for (AppVulnerability finding : findings) {
                    if (finding != null) {
                        appVulnerabilityList.add(finding);
                    }
                }
            }

            TargetApplicationScanningResult targetApplicationScanningResult = new TargetApplicationScanningResult(appVulnerabilityList, permissionList, packageName, versionCode, versionString, averageCvss, appDetails, status);
            targetApplicationScanningResult.setJsonObject(jsonObject);
            return targetApplicationScanningResult;
        }
    }

    public static int[] getFindingSummary(TargetApplicationScanningResult targetApplicationScanningResult) {
        List<AppVulnerability> appVulnerabilityList = targetApplicationScanningResult.getAppVulnerabilityList();
        int[] summary = {0, 0, 0, 0};
        for (AppVulnerability appVulnerability: appVulnerabilityList) {
            switch (appVulnerability.getLevel()) {
                case "high":
                    summary[0]++;
                    break;
                case "warning":
                    summary[1]++;
                    break;
                case "good":
                    summary[2]++;
                    break;
                case "info":
                    summary[3]++;
                    break;
            }
        }
        return summary;
    }

    public static int[] getPermissionSummary(TargetApplicationScanningResult targetApplicationScanningResult) {
        List<Permission> permissionList = targetApplicationScanningResult.getPermissionList();
        int[] summary = {0, 0, 0, 0};
        for (Permission permission: permissionList) {
            switch (permission.getLevel()) {
                case "normal":
                    summary[0]++;
                    break;
                case "signature":
                    summary[1]++;
                    break;
                case "dangerous":
                    summary[2]++;
                    break;
                case "special":
                    summary[3]++;
                    break;
            }
        }
        return summary;
    }

    public static Map<String, List<AppVulnerability>> getOwaspSummary(TargetApplicationScanningResult targetApplicationScanningResult) {
        Map<String, List<AppVulnerability>> owaspSummary = new TreeMap<String, List<AppVulnerability>>();
        List<AppVulnerability> appVulnerabilityList = targetApplicationScanningResult.getAppVulnerabilityList();
        for(AppVulnerability appVulnerability: appVulnerabilityList) {
            if (appVulnerability.getOwaspId() != null) {
                if (owaspSummary.containsKey(appVulnerability.getOwaspId())) {
                    owaspSummary.get(appVulnerability.getOwaspId()).add(appVulnerability);
                } else {
                    List<AppVulnerability> vulnerabilities = new ArrayList<AppVulnerability>();
                    vulnerabilities.add(appVulnerability);
                    owaspSummary.put(appVulnerability.getOwaspId(), vulnerabilities);
                }
            }
        }
        return owaspSummary;
    }
}
