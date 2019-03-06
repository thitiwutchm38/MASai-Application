package com.example.bookthiti.masai2.database.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.bookthiti.masai2.database.dao.TestingDao;
import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.database.model.TestingEntity;

@Database(entities = {TestingEntity.class, ActivityLogEntity.class},
        version = 1,
        exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {
    public abstract TestingDao testingDao();
}
