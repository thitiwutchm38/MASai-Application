package com.example.bookthiti.masai2.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.bookthiti.masai2.database.util.DateTimeConverter;

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
}
