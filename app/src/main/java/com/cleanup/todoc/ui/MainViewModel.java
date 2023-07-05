package com.cleanup.todoc.ui;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repository.Repository;
import com.cleanup.todoc.ui.utils.SortMethod;
import com.cleanup.todoc.ui.utils.TasksComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainViewModel extends ViewModel {

    private final Repository mRepository;
    //private final LiveData<List<TaskViewState>> liveDataForTaskViewState;
    private final MediatorLiveData<List<TaskViewState>> taskListMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<SortMethod> filterMutableLiveData = new MutableLiveData<>(SortMethod.OLD_FIRST);

    public MainViewModel(Repository repository) {
        mRepository = repository;
//        liveDataForTaskViewState = Transformations.map(mRepository.getTasksLiveData(), tasks-> parseIntoViewState(tasks));
        LiveData<List<Task>> taskList = mRepository.getTasksLiveData();
        LiveData<List<Project>> projectsMutableLiveData = mRepository.getProjects();
        //combine in the callback's Observer data for the original list
        taskListMediatorLiveData.addSource(
                taskList,
                tasks -> combine(tasks, filterMutableLiveData.getValue(), projectsMutableLiveData.getValue()));
        taskListMediatorLiveData.addSource(
                projectsMutableLiveData,
                projects -> combine(taskList.getValue(), filterMutableLiveData.getValue(), projects));
        taskListMediatorLiveData.addSource(
                filterMutableLiveData,
                filter -> combine(taskList.getValue(), filter, projectsMutableLiveData.getValue()));
    }

    private void combine(List<Task> tasks, SortMethod filter, List<Project> projects) {
        if (tasks == null || projects == null) {
            return;
        }
        //sort according to filter
        List<TaskViewState> tasksFilterVS;
        switch (filter) {
            case ALPHABETICAL:
                Collections.sort(tasks, new TasksComparator.TaskAZComparator());
                break;
            case ALPHABETICAL_INVERTED:
                Collections.sort(tasks, new TasksComparator.TaskZAComparator());
                break;
            case RECENT_FIRST:
                Collections.sort(tasks, new TasksComparator.TaskRecentComparator());
                break;
            case OLD_FIRST:
                Collections.sort(tasks, new TasksComparator.TaskOldComparator());
                break;
        }
        tasksFilterVS = parseIntoViewState(tasks, projects);
        taskListMediatorLiveData.setValue(tasksFilterVS);

    }

    /**
     * Get all task
     *
     * @return list of task
     */
    public LiveData<List<TaskViewState>> getTasks() {
        return taskListMediatorLiveData;
    }

    /**
     * Get all projects
     *
     * @return List of projects
     */
    public LiveData<List<Project>> getProjects() {
        return mRepository.getProjects();
    }

    /**
     * To parse into TaskViewState for UI
     *
     * @param list     af task
     * @param projects
     * @return list of task fit into taskViewState model
     */
    private List<TaskViewState> parseIntoViewState(List<Task> list, List<Project> projects) {
        List<TaskViewState> taskViewStateList = new ArrayList<>();

        for (Task task : list) {
            Project project = getProjectById(projects, task.getProjectId());
            if (project != null) {
                taskViewStateList.add(new TaskViewState(
                        task.getId(),
                        task.getName(),
                        project.getColor()
                ));
            }

        }
        return taskViewStateList;
    }

    private Project getProjectById(List<Project> projects, long projectId) {
        for (Project project : projects) {
            if (project.getId() == projectId) {
                return project;
            }
        }
        return null;
    }

    /**
     * Create a task
     *
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

    //TODO : do filter
    public void resetFilter(SortMethod none) {
        filterMutableLiveData.setValue(none);
    }

    public void sortAlphabetical(SortMethod alphabetical) {
        filterMutableLiveData.setValue(alphabetical);
    }

    public void sortAlphabeticalInverted(SortMethod invertedAlphabetical) {
        filterMutableLiveData.setValue(invertedAlphabetical);
    }

    public void sortOlderFirst(SortMethod olderFirst) {
        filterMutableLiveData.setValue(olderFirst);
    }

    public void sortRecentFirst(SortMethod recentFirst) {
        filterMutableLiveData.setValue(recentFirst);
    }

}
