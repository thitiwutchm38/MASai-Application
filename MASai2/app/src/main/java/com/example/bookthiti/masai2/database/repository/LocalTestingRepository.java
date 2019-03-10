package com.example.bookthiti.masai2.database.repository;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import com.example.bookthiti.masai2.database.dao.ActivityLogDao;
import com.example.bookthiti.masai2.database.dao.TestingDao;
import com.example.bookthiti.masai2.database.db.MasaiLocalDatabase;
import com.example.bookthiti.masai2.database.model.ActivityLogEntity;
import com.example.bookthiti.masai2.database.model.TestingEntity;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class LocalTestingRepository {

    private MasaiLocalDatabase masaiLocalDatabase;
    private TestingDao testingDao;
    private ActivityLogDao activityLogDao;
    private LiveData<List<TestingEntity>> allTestingEntities;
    private LiveData<List<ActivityLogEntity>> allActivityLogEntities;

    public LocalTestingRepository(Context context) {
        masaiLocalDatabase = MasaiLocalDatabase.getInstance(context);
        testingDao = masaiLocalDatabase.testingDao();
        activityLogDao = masaiLocalDatabase.activityLogDao();
        allTestingEntities = testingDao.fetchAllTestings();
        allActivityLogEntities =activityLogDao.fetchAllActivityLogEntities();
    }

    public void closeDatabase() throws IOException {
        masaiLocalDatabase.close();
    }

    public void setAllTestingEntities(LiveData<List<TestingEntity>> allTestingEntities) {
        this.allTestingEntities = allTestingEntities;
    }

    public LiveData<List<ActivityLogEntity>> getAllActivityLogEntities() {
        return allActivityLogEntities;
    }

    public void setAllActivityLogEntities(LiveData<List<ActivityLogEntity>> allActivityLogEntities) {
        this.allActivityLogEntities = allActivityLogEntities;
    }

    public void insertTesting(String title, Date createdAt) {
        TestingEntity testingEntity = new TestingEntity();
        testingEntity.setTitle(title);
        testingEntity.setCreatedAt(createdAt);
        testingEntity.setModifiedAt(createdAt);
        insertTesting(testingEntity);
    }

    public void insertTesting(TestingEntity... testingEntities) {
        new InsertTestingAsyncTask(testingDao).execute(testingEntities);
    }

    public void updateTesting(TestingEntity testingEntity) {
        testingEntity.setModifiedAt(Calendar.getInstance().getTime());
        new UpdateTestingAsyncTask(testingDao).execute(testingEntity);
    }

    public void deleteTesting(TestingEntity... testingEntity) {
        new DeleteTestingAsyncTask(testingDao).execute(testingEntity);
    }

    public LiveData<List<TestingEntity>> getAllTestingEntities() {
        return masaiLocalDatabase.testingDao().fetchAllTestings();
    }

    public LiveData<TestingEntity> getTesting(int id) {
        return masaiLocalDatabase.testingDao().getTestingById(id);
    }

    private static class InsertTestingAsyncTask extends AsyncTask<TestingEntity, Void, Void> {
        private TestingDao asyncTestingDao;

        public InsertTestingAsyncTask(TestingDao dao) {
            asyncTestingDao = dao;
        }

        @Override
        protected Void doInBackground(TestingEntity... testingEntities) {
            asyncTestingDao.insertTesting(testingEntities);
            return null;
        }
    }

    private static class UpdateTestingAsyncTask extends AsyncTask<TestingEntity, Void, Void> {
        private TestingDao asyncTestingDao;

        public UpdateTestingAsyncTask(TestingDao dao) {
            asyncTestingDao = dao;
        }

        @Override
        protected Void doInBackground(TestingEntity... testingEntities) {
            asyncTestingDao.updateTesting(testingEntities);
            return null;
        }
    }

    private static class DeleteTestingAsyncTask extends AsyncTask<TestingEntity, Void, Void> {
        private TestingDao asyncTestingDao;

        public DeleteTestingAsyncTask(TestingDao dao) {
            asyncTestingDao = dao;
        }

        @Override
        protected Void doInBackground(TestingEntity... testingEntities) {
            asyncTestingDao.deleteTesting(testingEntities);
            return null;
        }
    }
}
