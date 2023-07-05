package com.cleanup.todoc.data;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.AppDatabase;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class TaskDaoTest {

    // FOR DATA
    private AppDatabase database;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),

                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        database.projectDao().createProject(new Project(
                PROJECT_ID,
                "Projet Tartampion",
                0xFFEADAD1));
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    //DATA SET FOR TEST
    private static long PROJECT_ID = 1L;
    private static long TASK_ID = 2;
    private static Task TASK_DEMO = new Task(TASK_ID, PROJECT_ID, "Task test", 2);


    @Test
    public void createTask_and_getTasks_WithSuccess() throws InterruptedException {
        //WHEN add a task in database
        database.taskDao().createTask(TASK_DEMO);
        //THEN
        List<Task> taskList = LiveDataTestUtils.getOrAwaitValue(this.database.taskDao().getTasks());
        Truth.assertThat(taskList).contains(TASK_DEMO);
    }

    @Test
    public void deleteTask_withSuccess() throws InterruptedException {
        //WHEN
        database.taskDao().deleteTask(TASK_ID);
        //THEN
        List<Task> taskList = LiveDataTestUtils.getOrAwaitValue(this.database.taskDao().getTasks());
        Truth.assertThat(taskList).doesNotContain(TASK_DEMO);
    }
}
