package com.example.bookthiti.masai2.database.repository;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.bookthiti.masai2.database.db.LocalDatabase;

public class LocalTestingRepository {
    private static final String DB_NAME = "db_local_testing";

    private LocalDatabase localDatabase;

    public LocalTestingRepository(Context context) {
        localDatabase = Room.databaseBuilder(context, LocalDatabase.class, DB_NAME).build();
    }

        
}
