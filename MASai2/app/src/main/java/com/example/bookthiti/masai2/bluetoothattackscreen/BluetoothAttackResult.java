package com.example.bookthiti.masai2.bluetoothattackscreen;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class BluetoothAttackResult {

    private BluetoothDeviceModel bluetoothDeviceModel;
    private String status;

    public BluetoothAttackResult(BluetoothDeviceModel bluetoothDeviceModel, String status) {
        this.bluetoothDeviceModel = bluetoothDeviceModel;
        this.status = status;
    }

    public BluetoothAttackResult() {
    }

    public BluetoothDeviceModel getBluetoothDeviceModel() {
        return bluetoothDeviceModel;
    }

    public void setBluetoothDeviceModel(BluetoothDeviceModel bluetoothDeviceModel) {
        this.bluetoothDeviceModel = bluetoothDeviceModel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class BluetoothAttackResultDeserializer implements JsonDeserializer<BluetoothAttackResult> {

        @Override
        public BluetoothAttackResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            String status = jsonObject.get("status") != null && !jsonObject.get("status").isJsonNull() ? jsonObject.get("status").getAsString() : null;
            BluetoothDeviceModel bluetoothDeviceModel = null;
            if (jsonObject.get("bluetoothDevice") != null && !jsonObject.get("bluetoothDevice").isJsonNull()) {
                bluetoothDeviceModel = context.deserialize(jsonObject.get("bluetoothDevice"), BluetoothDeviceModel.class);
            }
            return new BluetoothAttackResult(bluetoothDeviceModel, status);
        }
    }
}
