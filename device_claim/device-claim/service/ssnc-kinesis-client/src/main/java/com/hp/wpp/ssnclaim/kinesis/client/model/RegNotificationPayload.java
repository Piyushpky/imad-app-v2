package com.hp.wpp.ssnclaim.kinesis.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

/**
 * Created by karanam on 1/23/2018.
 */
public class RegNotificationPayload {

    @JsonProperty("snkey")
    private String snkey;

    @JsonProperty("cloudId")

    private String cloudId;

    @JsonProperty("created_at")
    private Timestamp createdAt;

    public String getSnkey() {
        return snkey;
    }

    public void setSnkey(String snkey) {
        this.snkey = snkey;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "RegNotificationPayload{" +
                "snkey='" + snkey + '\'' +
                ", cloudId='" + cloudId + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
