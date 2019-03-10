package com.example.bookthiti.masai2.mainscreen;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.bookthiti.masai2.database.model.TestingEntity;
import com.example.bookthiti.masai2.database.repository.LocalTestingRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainAcitivtyViewModel extends AndroidViewModel {

    private LocalTestingRepository localTestingRepository;

    private LiveData<List<TestingEntity>> allTestingEntities;

    public MainAcitivtyViewModel(@NonNull Application application) {
        super(application);
        localTestingRepository = new LocalTestingRepository(application);
        allTestingEntities = localTestingRepository.getAllTestingEntities();
    }

    public LiveData<List<TestingEntity>> getAllTestingEntities() {
        return allTestingEntities;
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
}
