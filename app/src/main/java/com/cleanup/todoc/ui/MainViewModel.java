package com.cleanup.todoc.ui;

import static com.cleanup.todoc.ui.utils.SortMethod.ALPHABETICAL;
import static com.cleanup.todoc.ui.utils.SortMethod.ALPHABETICAL_INVERTED;
import static com.cleanup.todoc.ui.utils.SortMethod.OLD_FIRST;
import static com.cleanup.todoc.ui.utils.SortMethod.RECENT_FIRST;
import static kotlinx.coroutines.flow.FlowKt.combine;

import androidx.lifecycle.LiveData;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
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
    private final MutableLiveData<SortMethod> filterMutableLiveData = new MutableLiveData<>();

    public MainViewModel(Repository repository) {
        mRepository = repository;
//        liveDataForTaskViewState = Transformations.map(mRepository.getTasksLiveData(), tasks-> parseIntoViewState(tasks));
        LiveData<List<Task>> taskList = mRepository.getTasksLiveData();

        //combine in the callback's Observer data for the original list
       taskListMediatorLiveData.addSource(taskList, tasks -> combine(tasks,filterMutableLiveData.getValue()));
       taskListMediatorLiveData.addSource(filterMutableLiveData, filter -> combine(taskList.getValue(), filter));
    }

    private void combine(List<Task> tasks, SortMethod filter) {
        if (tasks == null) {
            return;
        }
        List<Task> filteredTasks = new ArrayList<>();
        //sort according to filter
        if (filter == SortMethod.NONE || filter == null){
            filteredTasks = tasks;
            List<TaskViewState> filteredTasksVS = parseIntoViewState(filteredTasks);
            taskListMediatorLiveData.setValue(filteredTasksVS);
        } else {
            List<TaskViewState> tasksFilterVS;
            switch (filter) {
                case ALPHABETICAL:
                    Collections.sort(tasks, new TasksComparator.TaskAZComparator());
                    tasksFilterVS = parseIntoViewState(tasks);
                    taskListMediatorLiveData.setValue(tasksFilterVS);
                    break;
                case ALPHABETICAL_INVERTED:
                    Collections.sort(tasks, new TasksComparator.TaskZAComparator());
                    tasksFilterVS = parseIntoViewState(tasks);
                    taskListMediatorLiveData.setValue(tasksFilterVS);
                    break;
                case RECENT_FIRST:
                    Collections.sort(tasks, new TasksComparator.TaskRecentComparator());
                    tasksFilterVS = parseIntoViewState(tasks);
                    taskListMediatorLiveData.setValue(tasksFilterVS);
                    break;
                case OLD_FIRST:
                    Collections.sort(tasks, new TasksComparator.TaskOldComparator());
                    tasksFilterVS = parseIntoViewState(tasks);
                    taskListMediatorLiveData.setValue(tasksFilterVS);
                    break;
            }
        }
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
     * @return List of projects
     */
    public List<Project> getProjects(){
        return mRepository.getProjects();
    }

    /**
     * To parse into TaskViewState for UI
     *
     * @param list af task
     * @return list of task fit into taskViewState model
     */
    private List<TaskViewState> parseIntoViewState(List<Task> list) {
        List<TaskViewState> taskViewStateList = new ArrayList<>();

        for (Task task : list) {
            Project project = mRepository.getProjectById(task.getProjectId());
            assert project != null;
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
