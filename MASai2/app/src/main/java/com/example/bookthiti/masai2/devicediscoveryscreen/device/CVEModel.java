package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CVEModel implements Parcelable {

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

    public CVEModel(Parcel in) {
        id = in.readString();
        description = in.readString();
        severity = in.readTypedObject(Severity.CREATOR);
    }

    public static final Creator<CVEModel> CREATOR = new Creator<CVEModel>() {
        @Override
        public CVEModel createFromParcel(Parcel in) {
            return new CVEModel(in);
        }

        @Override
        public CVEModel[] newArray(int size) {
            return new CVEModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(description);
        parcel.writeTypedObject(severity, 0);
    }

    public static class Severity implements Parcelable{
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

        protected Severity(Parcel in) {
            severity = in.readString();
            topVulnerable = in.readByte() != 0;
            topAlert = in.readByte() != 0;
            cvssList = new ArrayList<CVSS>();
            in.readTypedList(cvssList, CVSS.CREATOR);
        }

        public static final Creator<Severity> CREATOR = new Creator<Severity>() {
            @Override
            public Severity createFromParcel(Parcel in) {
                return new Severity(in);
            }

            @Override
            public Severity[] newArray(int size) {
                return new Severity[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(severity);
            parcel.writeByte((byte) (topVulnerable ? 1 : 0));
            parcel.writeByte((byte) (topAlert ? 1 : 0));
            parcel.writeTypedList(cvssList);
        }
    }

    public static class CVSS implements Parcelable{
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

        protected CVSS(Parcel in) {
            accessComplexity = in.readString();
            accessVector = in.readString();
            authentication = in.readString();
            availability = in.readString();
            confidentiality = in.readString();
            integrity = in.readString();
            vector = in.readString();
            base = in.readFloat();
            exploitability = in.readFloat();
            impact = in.readFloat();
        }

        public static final Creator<CVSS> CREATOR = new Creator<CVSS>() {
            @Override
            public CVSS createFromParcel(Parcel in) {
                return new CVSS(in);
            }

            @Override
            public CVSS[] newArray(int size) {
                return new CVSS[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(accessComplexity);
            parcel.writeString(accessVector);
            parcel.writeString(authentication);
            parcel.writeString(availability);
            parcel.writeString(confidentiality);
            parcel.writeString(integrity);
            parcel.writeString(vector);
            parcel.writeFloat(base);
            parcel.writeFloat(exploitability);
            parcel.writeFloat(impact);
        }
    }

    public static class SeverityDeserializer implements JsonDeserializer<Severity> {
        @Override
        public Severity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String severity = jsonObject.get("severity") != null && !jsonObject.get("severity").isJsonNull() ? jsonObject.get("severity").getAsString() : null;
            boolean topVulnerable = jsonObject.get("topVulnerable") != null && !jsonObject.get("topVulnerable").isJsonNull() ? jsonObject.get("topVulnerable").getAsBoolean() : null;
            boolean topAlert = jsonObject.get("topAlert") != null && !jsonObject.get("topAlert").isJsonNull() ? jsonObject.get("topAlert").getAsBoolean() : null;
            List<CVSS> cvssList = new ArrayList<CVSS>();
            if (jsonObject.get("services") != null && !jsonObject.get("services").isJsonNull()) {
                CVSS[] cvsses = context.deserialize(jsonObject.get("cvss2"), CVSS[].class);
                cvssList = Arrays.asList(cvsses);
            }
            return new Severity(severity, topVulnerable, topAlert, cvssList);
        }
    }
}
