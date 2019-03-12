package com.example.bookthiti.masai2.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.bookthiti.masai2.database.util.DateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "activity_log",
        foreignKeys = @ForeignKey(entity = TestingEntity.class,
                parentColumns = "id",
                childColumns = "testing_id",
                onDelete = CASCADE))
public class ActivityLogEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private long id;

    @NonNull
    @ColumnInfo(name = "testing_id")
    private long testingId;

    @ColumnInfo(name = "start_time")
    private Date startTime;

    @ColumnInfo(name = "finish_time")
    private Date finishTime;

    private String name;
    private String status;

    @ColumnInfo(name = "json_output")
    private String jsonOutput;

    public ActivityLogEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTestingId() {
        return testingId;
    }

    public void setTestingId(long testingId) {
        this.testingId = testingId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJsonOutput() {
        return jsonOutput;
    }

    public void setJsonOutput(String jsonOutput) {
        this.jsonOutput = jsonOutput;
    }

    public static class ActivityLogEntitySerializer implements JsonSerializer<ActivityLogEntity> {
        @Override
        public JsonElement serialize(ActivityLogEntity src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("id", new JsonPrimitive(src.id));
            jsonObject.add("testingId", new JsonPrimitive(src.testingId));
            jsonObject.addProperty("startTime", src.startTime.toString());
            jsonObject.addProperty("finishTime", src.finishTime.toString());
            jsonObject.addProperty("name", src.name);
            jsonObject.addProperty("status", src.status);
            jsonObject.addProperty("jsonOutput", src.jsonOutput);
            return jsonObject;
        }
    }
}
