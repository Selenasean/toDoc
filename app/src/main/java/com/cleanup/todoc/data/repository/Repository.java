package com.cleanup.todoc.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Data source
 */
public class Repository {

    private final MutableLiveData<List<Task>> tasksMutableLiveData = new MutableLiveData<>();

    private static final MutableLiveData<List<Project>> projectsMutableLiveData = new MutableLiveData<>();

    private static MutableLiveData<Project> mutableLiveDataForProject = new MutableLiveData<>();


    /**
     * Get a list of tasks type LiveData
     * @return a list of tasks
     */
    public LiveData<List<Task>> getTasksLiveData() {
        return tasksMutableLiveData;
    }


    /**
     * Returns all the projects of the application.
     * @return all the projects of the application
     */
    @NonNull
    public LiveData<List<Project>> getProjectsLiveData(){
        projectsMutableLiveData.setValue(Arrays.asList(
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2)));
        return projectsMutableLiveData;
    }


    /**
     * Returns the project with the given unique identifier, or null if no project with that
     * identifier can be found.
     *
     * @param id the unique identifier of the project to return
     * @return the project with the given unique identifier, or null if it has not been found
     */
    @Nullable
    public LiveData<Project> getProjectById(long id) {
        List<Project> projectsList = projectsMutableLiveData.getValue();
        assert projectsList != null;
        for (Project project : projectsList) {
            if (project.getId() == id){
                mutableLiveDataForProject.setValue(project);
                return mutableLiveDataForProject;
            }
        }
        return null;
    }

    /**
     * Create a Task
     */
    public void createTask(Task task){
        List<Task> taskList = tasksMutableLiveData.getValue();
        assert taskList != null;
        taskList.add(task);
        tasksMutableLiveData.setValue(taskList);
    }



    /**
     * Comparator to sort task from A to Z
     */
    public static class TaskAZComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return left.getName().compareTo(right.getName());
        }
    }

    /**
     * Comparator to sort task from Z to A
     */
    public static class TaskZAComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return right.getName().compareTo(left.getName());
        }
    }

    /**
     * Comparator to sort task from last created to first created
     */
    public static class TaskRecentComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (right.getCreationTimestamp() - left.getCreationTimestamp());
        }
    }

    /**
     * Comparator to sort task from first created to last created
     */
    public static class TaskOldComparator implements Comparator<Task> {
        @Override
        public int compare(Task left, Task right) {
            return (int) (left.getCreationTimestamp() - right.getCreationTimestamp());
        }
    }
}
