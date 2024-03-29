package com.cleanup.todoc.data.repository;


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
     * @return a list of tasks -type Livedata-
     */
    public LiveData<List<Task>> getTasksLiveData() {
        return database.taskDao().getTasks();
    }

    /**
     * Returns all the projects of the application.
     * @return all the projects of the application -type LiveData-
     */
    public LiveData<List<Project>> getProjects(){
        return database.projectDao().getProjects();
    }


    /**
     * Create a Task
     * @param task that the user wants to create
     */
    public void createTask(Task task){
        executor.execute(()->
                database.taskDao().createTask(task));
    }

    /**
     * Delete a task
     * @param taskId of the task the user wants to delete
     */
    public void deleteTask(long taskId){
        executor.execute(()->
                database.taskDao().deleteTask(taskId));
    }

}
