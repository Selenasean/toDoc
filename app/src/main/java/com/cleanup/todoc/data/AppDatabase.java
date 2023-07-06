package com.cleanup.todoc.data;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cleanup.todoc.data.dao.ProjectDao;
import com.cleanup.todoc.data.dao.TaskDao;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;

import java.util.concurrent.Executors;

/**
 * Abstract class that links DAO & configure our dataBase
 */
@Database(entities = {Task.class, Project.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    //DAO
    public abstract ProjectDao projectDao();
    public abstract TaskDao taskDao();

    //SINGLETON
    private static volatile AppDatabase INSTANCE;

    //INSTANCE
    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "MyDatabase.db")
                            .addCallback(prepopulateDatabase())
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    private static Callback prepopulateDatabase(){
        return new Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db){
                super.onCreate(db);

                //create projects in database - they are constant
                Executors.newSingleThreadExecutor().execute(()-> {
                        INSTANCE.projectDao().createProject(new Project(1L, "Projet Tartampion", 0xFFEADAD1));
                        INSTANCE.projectDao().createProject(new Project(2L, "Projet Lucidia", 0xFFB4CDBA));
                        INSTANCE.projectDao().createProject(new Project(3L, "Projet Circus", 0xFFA3CED2));
                });
            }
        };
    }
}
