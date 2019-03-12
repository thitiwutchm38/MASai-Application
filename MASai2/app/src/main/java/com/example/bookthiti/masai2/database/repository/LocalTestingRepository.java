package com.example.bookthiti.masai2.database.repository;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
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
import java.util.concurrent.ExecutionException;

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
        return allTestingEntities;
    }

    public LiveData<TestingEntity> getTesting(long id) {
        return testingDao.getTestingById(id);
    }

    public long[] insertActivityLogEntity(ActivityLogEntity... activityLogEntities) {
        try {
            return new InsertActivityLogAsyncTask(activityLogDao).execute(activityLogEntities).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new long[0];
    }

    public long insertActivityLogEntity(String name, String status, String jsonOutput, long testingId, Date startTime) {
        ActivityLogEntity activityLogEntity = new ActivityLogEntity();
        activityLogEntity.setName(name);
        activityLogEntity.setStatus(status);
        activityLogEntity.setJsonOutput(jsonOutput);
        activityLogEntity.setTestingId(testingId);
        activityLogEntity.setStartTime(startTime);
        long[] longs = insertActivityLogEntity(activityLogEntity);
        if (longs.length == 1) {
            return longs[0];
        } else {
            return -1;
        }
    }

    public void deleteActivityLogEntity(ActivityLogEntity... activityLogEntities) {
        new DeleteActivityLogAsyncTask(activityLogDao).execute(activityLogEntities);
    }

    public void updateActivityLogEntity(ActivityLogEntity... activityLogEntities) {
        new UpdateActivityLogAsyncTask(activityLogDao).execute(activityLogEntities);
    }

    public void updateActivityLogEntity(long id, String status, String jsonOutput, Date finishTime) {
        new UpdateActivityLogByIdAsyncTask(activityLogDao, id, status, jsonOutput, finishTime).execute();
    }

    public LiveData<ActivityLogEntity> getActivityLogEntityById(long id) {
        return activityLogDao.getActivityLogById(id);
    }

    public LiveData<List<ActivityLogEntity>> getAllActivityLogEntities() {
        return allActivityLogEntities;
    }

    public LiveData<List<ActivityLogEntity>> getActivityLogEntitiesByTestingId(long testingId) {
        return activityLogDao.getActivityLogEntitiesByTestingId(testingId);
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

    private static class InsertActivityLogAsyncTask extends AsyncTask<ActivityLogEntity, Void, long[]> {
        private ActivityLogDao asyncActivityLogDao;

        public InsertActivityLogAsyncTask(ActivityLogDao dao) {
            asyncActivityLogDao = dao;
        }

        @Override
        protected long[] doInBackground(ActivityLogEntity... activityLogEntities) {
            long[] id = asyncActivityLogDao.insertActivtyLog(activityLogEntities);
            return id;
        }
    }

    private static class DeleteActivityLogAsyncTask extends AsyncTask<ActivityLogEntity, Void, Void> {

        private ActivityLogDao activityLogDao;

        public DeleteActivityLogAsyncTask(ActivityLogDao dao) {
            activityLogDao = dao;
        }

        @Override
        protected Void doInBackground(ActivityLogEntity... activityLogEntities) {
            activityLogDao.deleteActivityLog(activityLogEntities);
            return null;
        }
    }

    private static class UpdateActivityLogAsyncTask extends AsyncTask<ActivityLogEntity, Void, Void> {

        private ActivityLogDao activityLogDao;

        public UpdateActivityLogAsyncTask(ActivityLogDao dao) {
            activityLogDao = dao;
        }

        @Override
        protected Void doInBackground(ActivityLogEntity... activityLogEntities) {
            activityLogDao.updateActivityLog(activityLogEntities);
            return null;
        }
    }

    private static class UpdateActivityLogByIdAsyncTask extends AsyncTask<Void, Void, Void> {
        private ActivityLogDao activityLogDao;
        private long id;
        private String status;
        private String jsonOutput;
        private Date finishTime;

        public UpdateActivityLogByIdAsyncTask(ActivityLogDao dao, long id, String status, String jsonOutput, Date finishTime) {
            activityLogDao = dao;
            this.id = id;
            this.status = status;
            this.jsonOutput = jsonOutput;
            this.finishTime = finishTime;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            activityLogDao.updateActivityLogById(id, status, jsonOutput, finishTime);
            return null;
        }
    }
}
