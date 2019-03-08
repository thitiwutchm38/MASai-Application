package com.example.bookthiti.masai2.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.bookthiti.masai2.database.model.TestingEntity;

import java.util.List;

@Dao
public interface TestingDao {

    @Insert
    void insertTesting(TestingEntity... testingEntities);

    @Query("SELECT * FROM testing ORDER BY created_at asc")
    LiveData<List<TestingEntity>> fetchAllTestings();

    @Query("SELECT * FROM testing WHERE id =:id")
    LiveData<TestingEntity> getTestingById(int id);

    @Update
    void updateTesting(TestingEntity... testingEntities);

    @Delete
    void deleteTesting(TestingEntity... testingEntities);
}
