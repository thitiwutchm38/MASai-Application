package com.example.bookthiti.masai2.database.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.bookthiti.masai2.database.model.ActivityLogEntity;

import java.util.Date;
import java.util.List;

@Dao
public interface ActivityLogDao {

    @Insert
    public long[] insertActivtyLog(ActivityLogEntity... activityLogEntities);

    @Query("SELECT * FROM activity_log ORDER BY start_time asc")
    public LiveData<List<ActivityLogEntity>> fetchAllActivityLogEntities();

    @Query("SELECT * FROM activity_log WHERE testing_id = :testingId")
    public LiveData<List<ActivityLogEntity>> getActivityLogEntitiesByTestingId(long testingId);

    @Query("SELECT * FROM activity_log WHERE id = :id")
    public LiveData<ActivityLogEntity> getActivityLogById(long id);

    @Update
    public void updateActivityLog(ActivityLogEntity... activityLogEntity);

    @Query("UPDATE activity_log SET status = :status, json_output = :jsonOutput, finish_time = :finishTime WHERE id = :id")
    public void updateActivityLogById(long id, String status, String jsonOutput, Date finishTime);

    @Delete
    public void deleteActivityLog(ActivityLogEntity... activityLogEntity);
}
