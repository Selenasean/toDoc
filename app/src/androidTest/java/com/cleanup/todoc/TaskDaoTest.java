package com.cleanup.todoc;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.AppDatabase;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    // FOR DATA
    private AppDatabase database;


    @Before
    public void initDb() throws Exception {

        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),

                        AppDatabase.class)
                .addCallback(new RoomDatabase.Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        Executors.newSingleThreadExecutor().execute(()->
                            database.projectDao().createProject(new Project(
                                    PROJECT_ID,
                                    "Projet Tartampion",
                                    0xFFEADAD1)));

                    }
                })

                .allowMainThreadQueries()

                .build();


    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    //DATA SET FOR TEST
    private static long PROJECT_ID = 1L;
    private static long TASK_ID;
    private static Task TASK_DEMO = new Task(TASK_ID, PROJECT_ID, "Task test", 2);


    @Test
    public void getTasksWithSuccess() {
        //add a task in database
        database.taskDao().createTask(TASK_DEMO);
        //THEN
//        List<Task> taskList = LiveDataTestUtils.getOrAwaitValue(this.database.taskDao().getTasks());
    }
}
