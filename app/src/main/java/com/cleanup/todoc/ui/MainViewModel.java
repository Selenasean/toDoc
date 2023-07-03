package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repository.Repository;
import com.cleanup.todoc.ui.utils.SortMethod;

import java.util.ArrayList;
import java.util.List;

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
     * Get all projects
     * @return List of projects
     */
    public List<Project> getProjects(){
        return mRepository.getProjects();
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
            //TODO : find how to get a projectById non type <liveData>
            Project project = mRepository.getProjectById(task.getProjectId());
            taskViewStateList.add(new TaskViewState(
                    task.getId(),
                    task.getName(),
                    project.getColor()
            ));
        }
        return taskViewStateList;
    }

    /**
     * Create a task
     * @param task we want to create
     */
    public void createTask(Task task) {
        mRepository.createTask(task);
    }

    /**
     * Delete a Task using his id
     *
     * @param taskId id of the task we want to delete
     */
    public void deleteTask(long taskId) {
        mRepository.deleteTask(taskId);
    }


    public SortMethod sortAlphabetical() {
        return SortMethod.ALPHABETICAL;
    }

    public SortMethod sortAlphabeticalInverted() {
        return SortMethod.ALPHABETICAL_INVERTED;
    }

    public SortMethod sortOlderFirst() {
        return SortMethod.OLD_FIRST;
    }

    public SortMethod sortRecentFirst() {
        return SortMethod.RECENT_FIRST;
    }

}
