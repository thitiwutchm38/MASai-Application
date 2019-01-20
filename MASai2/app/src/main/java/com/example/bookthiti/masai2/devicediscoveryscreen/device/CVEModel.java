package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class CVEModel {

    private String id;
    private String description;
    private Severity severity;

    public CVEModel(String id, String description, Severity severity) {
        this.id = id;
        this.description = description;
        this.severity = severity;
    }

    public CVEModel() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public static class Severity {
        private String severity;
        private boolean topVulnerable;
        private boolean topAlert;
        private List<CVSS> cvssList;

        public Severity(String severity, boolean topVulnerable, boolean topAlert, List<CVSS> cvssList) {
            this.severity = severity;
            this.topVulnerable = topVulnerable;
            this.topAlert = topAlert;
            this.cvssList = cvssList;
        }

        public Severity() {
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public boolean isTopVulnerable() {
            return topVulnerable;
        }

        public void setTopVulnerable(boolean topVulnerable) {
            this.topVulnerable = topVulnerable;
        }

        public boolean isTopAlert() {
            return topAlert;
        }

        public void setTopAlert(boolean topAlert) {
            this.topAlert = topAlert;
        }

        public List<CVSS> getCvssList() {
            return cvssList;
        }

        public void setCvssList(List<CVSS> cvssList) {
            this.cvssList = cvssList;
        }
    }

    public class CVSS {
        private String accessComplexity;
        private String accessVector;
        private String authentication;
        private String availability;
        private String confidentiality;
        private String integrity;
        private String vector;
        private float base;
        private float exploitability;
        private float impact;

        public CVSS(String accessComplexity, String accessVector, String authentication, String availability, String confidentiality, String integrity, String vector, float base, float exploitability, float impact) {
            this.accessComplexity = accessComplexity;
            this.accessVector = accessVector;
            this.authentication = authentication;
            this.availability = availability;
            this.confidentiality = confidentiality;
            this.integrity = integrity;
            this.vector = vector;
            this.base = base;
            this.exploitability = exploitability;
            this.impact = impact;
        }

        public String getAccessComplexity() {
            return accessComplexity;
        }

        public void setAccessComplexity(String accessComplexity) {
            this.accessComplexity = accessComplexity;
        }

        public String getAccessVector() {
            return accessVector;
        }

        public void setAccessVector(String accessVector) {
            this.accessVector = accessVector;
        }

        public String getAuthentication() {
            return authentication;
        }

        public void setAuthentication(String authentication) {
            this.authentication = authentication;
        }

        public String getAvailability() {
            return availability;
        }

        public void setAvailability(String availability) {
            this.availability = availability;
        }

        public String getConfidentiality() {
            return confidentiality;
        }

        public void setConfidentiality(String confidentiality) {
            this.confidentiality = confidentiality;
        }

        public String getIntegrity() {
            return integrity;
        }

        public void setIntegrity(String integrity) {
            this.integrity = integrity;
        }

        public String getVector() {
            return vector;
        }

        public void setVector(String vector) {
            this.vector = vector;
        }

        public float getBase() {
            return base;
        }

        public void setBase(float base) {
            this.base = base;
        }

        public float getExploitability() {
            return exploitability;
        }

        public void setExploitability(float exploitability) {
            this.exploitability = exploitability;
        }

        public float getImpact() {
            return impact;
        }

        public void setImpact(float impact) {
            this.impact = impact;
        }
    }

    public static class SeverityDeserializer implements JsonDeserializer<Severity> {
        @Override
        public Severity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String severity = jsonObject.get("severity").getAsString();
            boolean topVulnerable = jsonObject.get("topVulnerable").getAsBoolean();
            boolean topAlert = jsonObject.get("topAlert").getAsBoolean();
            CVSS[] cvsses = context.deserialize(jsonObject.get("cvss2"), CVSS[].class);
            List<CVSS> cvssList = Arrays.asList(cvsses);
            return new Severity(severity, topVulnerable, topAlert, cvssList);
        }
    }
}
