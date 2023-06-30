package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainViewModel extends ViewModel {

    private final Repository mRepository;

    private final LiveData<List<TaskViewState>> liveDataForTaskViewState;

    public MainViewModel(Repository repository) {
        mRepository = repository;
        liveDataForTaskViewState = Transformations.map(mRepository.getTasksLiveData(),tasks-> parseIntoViewState(tasks));
    }

    /**
     * Get all task
     *
     * @return list of task
     */
    public LiveData<List<TaskViewState>> getTasks() {
        return liveDataForTaskViewState;
    }

    /**
     * To parse into TaskViewState for UI
     *
     * @param list
     * @return
     */
    private List<TaskViewState> parseIntoViewState(List<Task> list) {
        List<TaskViewState> taskViewStateList = new ArrayList<>();

        for (Task task : list) {
            Project project = Objects.requireNonNull(mRepository.getProjectById(task.getProjectId()));
            taskViewStateList.add(new TaskViewState(
                    task.getId(),
                    task.getName(),
                    project.getColor()
            ));
        }
        return taskViewStateList;
    }

    public void createTask(Task task) {
        mRepository.createTask(task);
    }

    public void deleteTask(long taskId) {
        mRepository.deleteTask(taskId);
    }

    /**
     * Get all projects
     * @return List of projects
     */
    public LiveData<List<Project>> getProjects(){
        return null;
//        return mRepository.getProjects();
    }

    /**
     * Create a task
     */
}
