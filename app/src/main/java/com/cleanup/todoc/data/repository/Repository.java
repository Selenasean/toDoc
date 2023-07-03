package com.cleanup.todoc.data.repository;


import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.cleanup.todoc.data.AppDatabase;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;


import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Data source
 */
public class Repository {

    private final AppDatabase database;
    private final Executor executor = Executors.newSingleThreadExecutor();

    /**
     * Constructor
     * @param database Room
     */
    public Repository(AppDatabase database) {
        this.database = database;
    }


    /**
     * Get a list of tasks type LiveData
     * @return a list of tasks
     */
    public LiveData<List<Task>> getTasksLiveData() {
        return database.taskDao().getTasks();
    }

    /**
     * Returns all the projects of the application.
     * @return all the projects of the application
     */
    public List<Project> getProjects(){
        return  database.projectDao().getProjects();

//        return Arrays.asList(
//                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
//                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
//                new Project(3L, "Projet Circus", 0xFFA3CED2));
    }


    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id the unique identifier of the project to return
     * @return the project with the given unique identifier, or null if it has not been found
     */
    @Nullable
    public Project getProjectById(long id) {
       return database.projectDao().getProjectById(id);
    }

    /**
     * Create a Task
     */
    public void createTask(Task task){
        executor.execute(()->
                database.taskDao().createTask(task));
    }

    public void deleteTask(long taskId){
        executor.execute(()->
                database.taskDao().deleteTask(taskId));
    }

}
