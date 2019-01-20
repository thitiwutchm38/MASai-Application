package com.example.bookthiti.masai2.devicediscoveryscreen.device;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ServiceModel {

    private int port;
    private String name;
    private String protocol;
    private String state;
    private String product;
    private String version;
    private List<CVEModel> cves;
    private List<String> cpe;

    public ServiceModel(int port, String name, String protocol, String state, String product, String version, List<CVEModel> cves, List<String> cpe) {
        this.port = port;
        this.name = name;
        this.protocol = protocol;
        this.state = state;
        this.product = product;
        this.version = version;
        this.cves = cves;
        this.cpe = cpe;
    }

    public ServiceModel(int port, String name, String protocol, String state, String product, String version) {
        this.port = port;
        this.name = name;
        this.protocol = protocol;
        this.state = state;
        this.product = product;
        this.version = version;
        this.cves = new ArrayList<CVEModel>();
        this.cpe = new ArrayList<String>();
    }

    public ServiceModel() {

    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<CVEModel> getCves() {
        return cves;
    }

    public void setCves(List<CVEModel> cves) {
        this.cves = cves;
    }

    public List<String> getCpe() {
        return cpe;
    }

    public void setCpe(List<String> cpe) {
        this.cpe = cpe;
    }

    public static Comparator<ServiceModel> portNum_asc = new Comparator<ServiceModel>() {
        public int compare(ServiceModel s1, ServiceModel s2) {
            return s2.getPort() - s1.getPort();
        }
    };
    public static Comparator<ServiceModel> portNum_des = new Comparator<ServiceModel>() {

        public int compare(ServiceModel s1, ServiceModel s2) {
            return s1.getPort() - s2.getPort();
        }
    };


    public static Comparator<ServiceModel> portsName_asc = new Comparator<ServiceModel>() {

        public int compare(ServiceModel s1, ServiceModel s2) {
            String c1 = s1.getName();
            String c2 = s2.getName();
            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }
            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }
            return c1.compareTo(c2);

        }
    };
    public static Comparator<ServiceModel> portsName_des = new Comparator<ServiceModel>() {

        public int compare(ServiceModel s1, ServiceModel s2) {
            String c1 = s1.getName();
            String c2 = s2.getName();
            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }
            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }
            return c2.compareTo(c1);

        }
    };


    public static Comparator<ServiceModel> portsProtocal_asc = new Comparator<ServiceModel>() {

        public int compare(ServiceModel s1, ServiceModel s2) {
            String c1 = s1.getProtocol();
            String c2 = s2.getProtocol();
            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }
            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }
            return c1.compareTo(c2);

        }
    };
    public static Comparator<ServiceModel> portsProtocal_des = new Comparator<ServiceModel>() {

        public int compare(ServiceModel s1, ServiceModel s2) {
            String c1 = s1.getProtocol();
            String c2 = s2.getProtocol();
            if (c1.toLowerCase().equals(s1) && c2.toUpperCase().equals(s2)) {
                return 1;
            }
            if (c1.toUpperCase().equals(s1) && c2.toLowerCase().equals(s2)) {
                return -1;
            }
            return c2.compareTo(c1);

        }
    };

    public static class ServiceModelDeserializer implements JsonDeserializer<ServiceModel> {
        @Override
        public ServiceModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int port = jsonObject.get("port").getAsInt();
            String protocol = jsonObject.get("protocol").getAsString();
            String state = jsonObject.get("state").getAsString();
            String name = jsonObject.get("name").getAsString();
            String product = jsonObject.get("product") != null ? jsonObject.get("product").getAsString() : null;
            String version = jsonObject.get("version") != null ? jsonObject.get("version").getAsString() : null;
            String[] serviceModelArray =  context.deserialize(jsonObject.get("cpe"), String[].class);
            List<String> cpe = Arrays.asList(serviceModelArray);
            CVEModel[] cveModels = context.deserialize(jsonObject.get("cves"), CVEModel[].class);
            List<CVEModel> cves = Arrays.asList(cveModels);
            return new ServiceModel(port, name, protocol, state, product, version, cves, cpe);
        }
    }


}