package com.cleanup.todoc.data;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.utils.LiveDataTestUtils;
import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@RunWith(AndroidJUnit4.class)
public class ProjectDaoTest {
    // FOR DATA
    private AppDatabase database;

    @Before
    public void initDb() throws Exception {

        this.database = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),

                        AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        database.projectDao().createProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
    }

    @After
    public void closeDb() throws Exception {
        database.close();
    }

    //DATA FOR TEST
    private final Project PROJECT_IN_DB = new Project(1L, "Projet Tartampion", 0xFFEADAD1);
    private final Project PROJECT_DEMO = new Project(4L, "Projet Test",0xFFEADAD1 );

    @Test
    public void getAllProjects_withSuccess(){
        //WHEN
        List<Project> projectsList = database.projectDao().getProjects();
        //THEN
        Truth.assertThat(projectsList).containsExactly(PROJECT_IN_DB);
    }

    @Test
    public void getProjectById_withSuccess(){
        //WHEN
        Project recoveredProject = database.projectDao().getProjectById(PROJECT_IN_DB.getId());
        //THEN
        Truth.assertThat(PROJECT_IN_DB).isEqualTo(recoveredProject);
    }

    @Test
    public void createProject_withSuccess() {
        //WHEN
        database.projectDao().createProject(PROJECT_DEMO);
        //THEN
        List<Project> projectsList = database.projectDao().getProjects();
        Truth.assertThat(projectsList).containsExactlyElementsIn(Arrays.asList(PROJECT_IN_DB,PROJECT_DEMO));
    }
}
