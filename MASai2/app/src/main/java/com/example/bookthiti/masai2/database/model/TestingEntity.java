package com.example.bookthiti.masai2.database.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.example.bookthiti.masai2.database.util.DateTimeConverter;

import java.util.Date;

@Entity(tableName = "testing")
public class TestingEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    @ColumnInfo(name = "created_at")
    @TypeConverters({DateTimeConverter.class})
    private Date createdAt;

    public TestingEntity() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
