package com.example.bookthiti.masai2.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.database.model.TestingEntity;
import com.example.bookthiti.masai2.database.repository.LocalTestingRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MasaiViewModel extends AndroidViewModel {

    private LocalTestingRepository localTestingRepository;

    public MasaiViewModel(@NonNull Application application) {
        super(application);
        localTestingRepository = new LocalTestingRepository(application);
    }

    public LiveData<List<TestingEntity>> getAllTestingEntities() {
        return localTestingRepository.getAllTestingEntities();
    }

    public void insertTestingEntity(TestingEntity... testingEntities) {
        localTestingRepository.insertTesting(testingEntities);
    }

    public void insertTestingEntity(String title) {
        Date createdDate = Calendar.getInstance().getTime();
        localTestingRepository.insertTesting(title, createdDate);
    }

    public void deleteTestingEntity(TestingEntity... testingEntities) {
        localTestingRepository.deleteTesting(testingEntities);
    }

    public void updateTestingEntity(TestingEntity testingEntity) {
        Date modifiedAt = Calendar.getInstance().getTime();
        testingEntity.setModifiedAt(modifiedAt);
        localTestingRepository.updateTesting(testingEntity);
    }

    public LiveData<List<ActivityLogEntity>> getAllActivityLogEntities() {
        return localTestingRepository.getAllActivityLogEntities();
    }

    public LiveData<List<ActivityLogEntity>> getActivityLogEntitiesByTestingId(int testingId) {
        return localTestingRepository.getActivityLogEntitiesByTestingId(testingId);
    }

    public LiveData<ActivityLogEntity> getActivityLogEntityById(int id) {
        return localTestingRepository.getActivityLogEntityById(id);
    }

    public void insertActivityLogEntity(ActivityLogEntity... activityLogEntities) {
        localTestingRepository.insertActivityLogEntity(activityLogEntities);
    }

    public void insertActivityLogEntity(String name, String status, String jsonOutput, int testingId) {
        Date startTime = Calendar.getInstance().getTime();
        localTestingRepository.insertActivityLogEntity(name, status, jsonOutput, testingId, startTime);
    }

    public void deleteActivityLogEntity(ActivityLogEntity... activityLogEntities) {
        localTestingRepository.deleteActivityLogEntity(activityLogEntities);
    }

    public void updateActivityLogEntity(ActivityLogEntity activityLogEntity) {
        localTestingRepository.updateActivityLogEntity(activityLogEntity);
    }

    public void updateActivityLogEntity(ActivityLogEntity activityLogEntity, String status, String jsonOutput, Date finishTime) {
        activityLogEntity.setStatus(status);
        activityLogEntity.setJsonOutput(jsonOutput);
        activityLogEntity.setFinishTime(finishTime);
        localTestingRepository.updateActivityLogEntity(activityLogEntity);
    }
}
