package com.example.bookthiti.masai2.portattackscreen;

import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class PortAttackResult {

    private ServiceModel serviceModel;
    private String result;
    private String username;
    private String password;

    public PortAttackResult() {
    }

    public PortAttackResult(ServiceModel serviceModel, String result, String username, String password) {
        this.serviceModel = serviceModel;
        this.result = result;
        this.username = username;
        this.password = password;
    }

    public ServiceModel getServiceModel() {
        return serviceModel;
    }

    public void setServiceModel(ServiceModel serviceModel) {
        this.serviceModel = serviceModel;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class PortAttackResultDeserializer implements JsonDeserializer<PortAttackResult> {

        @Override
        public PortAttackResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String result = jsonObject.get("attackResult").getAsString();
            String username = jsonObject.get("username") != null && !jsonObject.get("username").isJsonNull() ? jsonObject.get("username").getAsString() : null;
            String password = jsonObject.get("password") != null && !jsonObject.get("password").isJsonNull() ? jsonObject.get("password").getAsString() : null;
            ServiceModel serviceModel = null;
            if (jsonObject.get("service") != null && !jsonObject.get("service").isJsonNull()) {
                serviceModel = context.deserialize(jsonObject.get("service"), ServiceModel.class);
            }
            return new PortAttackResult(serviceModel, result, username, password);
        }
    }
}
