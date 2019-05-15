package com.example.bookthiti.masai2;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.bookthiti.masai2.database.dao.ActivityLogDao;
import com.example.bookthiti.masai2.database.dao.TestingDao;
import com.example.bookthiti.masai2.database.db.MasaiLocalDatabase;
import com.example.bookthiti.masai2.database.model.TestingEntity;
import com.example.bookthiti.masai2.database.repository.LocalTestingRepository;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class SimpleEntityReadWriteTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private LocalTestingRepository localTestingRepository;
    private MasaiLocalDatabase masaiLocalDatabase;
    private TestingDao testingDao;
    private ActivityLogDao activityLogDao;
    private Context context;

    @Before
    public void createDB() {
        context = InstrumentationRegistry.getTargetContext();
//        localTestingRepository = new LocalTestingRepository(context, true);
//        masaiLocalDatabase = MasaiLocalDatabase.getInMemory(context);
        masaiLocalDatabase = Room.inMemoryDatabaseBuilder(context, MasaiLocalDatabase.class).allowMainThreadQueries().build();
        testingDao = masaiLocalDatabase.testingDao();
        activityLogDao = masaiLocalDatabase.activityLogDao();
    }

    @After
    public void closeDB() throws IOException {
        masaiLocalDatabase.close();
    }

    @Test
    public void writeTestingAndReadAllTestings() throws InterruptedException {
        // Context of the app under test.
        TestingEntity testingEntity1 = new TestingEntity();
        testingEntity1.setCreatedAt(Calendar.getInstance().getTime());
        testingEntity1.setModifiedAt(Calendar.getInstance().getTime());
        testingEntity1.setTitle("New Testing");
        testingDao.insertTesting(testingEntity1);
        List<TestingEntity> testingEntityList = LiveDataTestUtil.getValue(testingDao.fetchAllTestings());
        assertThat(testingEntityList.size(), Matchers.equalTo(1));
        assertThat(testingEntityList.get(0).getTitle(), Matchers.equalTo("New Testing"));
//        assertThat(testingEntityList.get(0).getTitle(), Matchers.equalTo("New Testing"));
//        localTestingRepository.insertTesting("New Testing", Calendar.getInstance().getTime());
//        localTestingRepository.insertTesting("Another New Testing", Calendar.getInstance().getTime());
//        List<TestingEntity> testingEntityList = localTestingRepository.getAllTestingEntities().getValue();
//        assertThat(testingEntityList.get(0).getTitle(), Matchers.equalTo("New Testing"));
//        assertThat(testingEntityList.get(1).getTitle(), Matchers.equalTo("Another New Testing"));
    }
}
