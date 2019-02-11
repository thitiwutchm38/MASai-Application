package com.example.bookthiti.masai2.routercrackingscreen;

import java.util.List;

public class CrackResult {
    private String status;
    private String type;
    private String bssid;
    private String essid;
    private List<String> keys;

    public CrackResult() {

    }

    public CrackResult(String status, String type, String bssid, String essid, List<String> keys) {
        this.status = status;
        this.type = type;
        this.bssid = bssid;
        this.essid = essid;
        this.keys = keys;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getKeys() {
        return keys;
    }

    public void setKeys(List<String> keys) {
        this.keys = keys;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getEssid() {
        return essid;
    }

    public void setEssid(String essid) {
        this.essid = essid;
    }
}
