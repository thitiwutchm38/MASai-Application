package com.example.bookthiti.masai2.database.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.bookthiti.masai2.database.dao.ActivityLogDao;
import com.example.bookthiti.masai2.database.dao.TestingDao;
import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.database.model.TestingEntity;
import com.example.bookthiti.masai2.database.util.DateTimeConverter;

@Database(entities = {TestingEntity.class, ActivityLogEntity.class},
        version = 1)
@TypeConverters({DateTimeConverter.class})
public abstract class MasaiLocalDatabase extends RoomDatabase {
    private static MasaiLocalDatabase INSTANCE;
    private static MasaiLocalDatabase IN_MEMORY_INSTANCE;
    private static final String DB_NAME = "db_local_testing.db";

    public abstract TestingDao testingDao();
    public abstract ActivityLogDao activityLogDao();

    public static MasaiLocalDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MasaiLocalDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =  Room.databaseBuilder(context, MasaiLocalDatabase.class, DB_NAME).build();
                }
            }
        }
        return INSTANCE;
    }

    public static MasaiLocalDatabase getInMemory(final Context context) {
        if (IN_MEMORY_INSTANCE == null) {
            synchronized (MasaiLocalDatabase.class) {
                if (IN_MEMORY_INSTANCE == null) {
                    IN_MEMORY_INSTANCE =  Room.inMemoryDatabaseBuilder(context, MasaiLocalDatabase.class).build();
                }
            }
        }
        return IN_MEMORY_INSTANCE;
    }
}
