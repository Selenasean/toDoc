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
    private final MediatorLiveData<List<TaskViewState>> taskListMediatorLiveData = new MediatorLiveData<>();
    private final MutableLiveData<SortMethod> sortingTypeMutableLiveData = new MutableLiveData<>(SortMethod.OLD_FIRST);

    /**
     * Constructor
     * @param repository
     */
    public MainViewModel(Repository repository) {
        mRepository = repository;
        LiveData<List<Task>> taskList = mRepository.getTasksLiveData();
        LiveData<List<Project>> projectsMutableLiveData = mRepository.getProjects();

        //combine in the callback's Observer data for the original list
        taskListMediatorLiveData.addSource(
                taskList,
                tasks -> combine(tasks, sortingTypeMutableLiveData.getValue(), projectsMutableLiveData.getValue()));
        //combine in the callback's Observer data for update list of projects
        taskListMediatorLiveData.addSource(
                projectsMutableLiveData,
                projects -> combine(taskList.getValue(), sortingTypeMutableLiveData.getValue(), projects));
        //combine in the callback's Observer data for update the sorting type
        taskListMediatorLiveData.addSource(
                sortingTypeMutableLiveData,
                sortingType -> combine(taskList.getValue(), sortingType, projectsMutableLiveData.getValue()));
    }


    /**
     * Method that combine all parameters that we need to :
     * <p>- Display a list of tasks from the database,</p>
     * <p>- Sort the list of tasks according to the sorting type,</p>
     * <p>- Use the projects to parse the list of tasks into a list of taskViewState</p>
     * @param tasks
     * @param sortingType
     * @param projects
     */
    private void combine(List<Task> tasks, SortMethod sortingType, List<Project> projects) {
        if (tasks == null || projects == null) {
            return;
        }
        //sort according to sortingType
        List<TaskViewState> tasksFilterVS;
        switch (sortingType) {
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
     * @return list of task -type LiveData-
     */
    public LiveData<List<TaskViewState>> getTasks() {
        return taskListMediatorLiveData;
    }

    /**
     * Get all projects
     *
     * @return List of projects -type LiveData-
     */
    public LiveData<List<Project>> getProjects() {
        return mRepository.getProjects();
    }

    /**
     * To parse into TaskViewState for UI
     *
     * @param list     af all task
     * @param projects list of all projects
     * @return list of task fit into taskViewState model
     */
    public List<TaskViewState> parseIntoViewState(List<Task> list, List<Project> projects) {
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

    /**
     * Allows to get a specific project linked to the interested task
     * @param projects list of all projects
     * @param projectId id of the project that is linked to the interested task
     * @return the specific project we are searching for
     */
    public Project getProjectById(List<Project> projects, long projectId) {
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

    //METHODS TO SORT

    /**
     * Sort by alphabetical
     * @param alphabetical
     */
    public void sortAlphabetical(SortMethod alphabetical) {
        sortingTypeMutableLiveData.setValue(alphabetical);
    }

    /**
     * Sort by alphabetical inverted
     * @param invertedAlphabetical
     */
    public void sortAlphabeticalInverted(SortMethod invertedAlphabetical) {
        sortingTypeMutableLiveData.setValue(invertedAlphabetical);
    }

    /**
     * Sort by the most older task first
     * @param olderFirst
     */
    public void sortOlderFirst(SortMethod olderFirst) {
        sortingTypeMutableLiveData.setValue(olderFirst);
    }

    /**
     * Sort by the most recent task first
     * @param recentFirst
     */
    public void sortRecentFirst(SortMethod recentFirst) {
        sortingTypeMutableLiveData.setValue(recentFirst);
    }

}
