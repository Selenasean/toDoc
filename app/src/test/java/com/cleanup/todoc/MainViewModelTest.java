package com.cleanup.todoc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.Utils.LiveDataTestUtils;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repository.Repository;
import com.cleanup.todoc.ui.MainViewModel;
import com.cleanup.todoc.ui.TaskViewState;
import com.cleanup.todoc.ui.utils.SortMethod;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Unit Test on the MainViewModel
 */
@RunWith(JUnit4.class)
public class MainViewModelTest {

    private final Repository repository = Mockito.mock(Repository.class);
    private MainViewModel viewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    //DATA FOR TEST
    private List<Task> tasks;
    private List<Project> projects;
    private List<TaskViewState> tasksViewState;
    private Task TASK_CREATED;

    @Before
    public void setup() {
       projects = Arrays.asList(
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2)
        );
        LiveData<List<Project>> projectsLiveData = new MutableLiveData<>(projects);
        Mockito.when(repository.getProjects()).thenReturn(projectsLiveData);

        tasks = Arrays.asList(
                new Task(1, 1L, "aaa", 1),
                new Task(2, 2L, "zzz",2),
                new Task(3, 3L, "bbb",3)

        );
        LiveData<List<Task>> tasksLiveData = new MutableLiveData<>(tasks);
        Mockito.when(repository.getTasksLiveData()).thenReturn(tasksLiveData);

        viewModel = new MainViewModel(repository);

        tasksViewState = Arrays.asList(
                new TaskViewState(1, "aaa", 0xFFEADAD1),
                new TaskViewState(2, "zzz", 0xFFB4CDBA),
                new TaskViewState(3, "bbb", 0xFFA3CED2)
        );

        TASK_CREATED = new Task(4,1L,"yyy", 4);

    }

    //TODO : test ViewModel methods

    @Test
    public void getProjectById_withSuccess() throws InterruptedException {
        // WHEN
        List<Project> projectsList = LiveDataTestUtils.getOrAwaitValue(viewModel.getProjects());
        Project project =  viewModel.getProjectById(projectsList, tasks.get(1).getProjectId());
        // THEN
        Truth.assertThat(project.getId()).isEqualTo(2L);
    }

    @Test
    public void parseList_intoViewState_withSuccess(){
        //WHEN
        List<TaskViewState> tasksViewState = viewModel.parseIntoViewState(tasks, projects);
        //THEN
        Truth.assertThat(tasksViewState).containsExactlyElementsIn(tasksViewState);
    }

    @Test
    public void sortByAlphabetical_shouldReturnList_sortByAlphabetical() throws InterruptedException {
        //WHEN
        viewModel.sortAlphabetical(SortMethod.ALPHABETICAL);
        //THEN
        List<TaskViewState> filteredList = LiveDataTestUtils.getOrAwaitValue(viewModel.getTasks());
        Truth.assertThat(filteredList).containsExactly(
                tasksViewState.get(0),
                tasksViewState.get(2),
                tasksViewState.get(1))
                .inOrder();
    }

    @Test
    public void sortByAlphabeticalInverted_shouldReturnList_sortByAlphabeticalInverted() throws InterruptedException {
        //WHEN
        viewModel.sortAlphabeticalInverted(SortMethod.ALPHABETICAL_INVERTED);
        //THEN
        List<TaskViewState> filteredList =  LiveDataTestUtils.getOrAwaitValue(viewModel.getTasks());
        Truth.assertThat(filteredList).containsExactly(
                tasksViewState.get(1),
                tasksViewState.get(2),
                tasksViewState.get(0))
                .inOrder();
    }

    @Test
    public void sortByOldestFirst_shouldReturnList_sortByOldestFirst() throws InterruptedException {
        //WHEN
        viewModel.sortOlderFirst(SortMethod.OLD_FIRST);
        //THEN
        List<TaskViewState> filteredList =  LiveDataTestUtils.getOrAwaitValue(viewModel.getTasks());
        Truth.assertThat(filteredList).containsExactly(
                        tasksViewState.get(0),
                        tasksViewState.get(1),
                        tasksViewState.get(2))
                .inOrder();
    }

    @Test
    public void sortByRecentFirst_shouldReturnList_sortByRecentFirst() throws InterruptedException {
        //WHEN
        viewModel.sortRecentFirst(SortMethod.RECENT_FIRST);
        //THEN
        List<TaskViewState> filteredList =  LiveDataTestUtils.getOrAwaitValue(viewModel.getTasks());
        Truth.assertThat(filteredList).containsExactly(
                        tasksViewState.get(2),
                        tasksViewState.get(1),
                        tasksViewState.get(0))
                .inOrder();
    }

    @Test
    public void deleteTask_shouldCall_theRepository() throws InterruptedException {
        //WHEN
        long TASK_ID = tasks.get(0).getId();
        viewModel.deleteTask(TASK_ID);
        //THEN the repository is called only once & it gets the same parameter as the viewModel
        Mockito.verify(repository, times(1)).deleteTask(TASK_ID);
        Mockito.verify(repository).deleteTask(eq(TASK_ID));
    }

    @Test
    public void createTask_shouldCall_theRepository() throws InterruptedException {
        //WHEN
        viewModel.createTask(TASK_CREATED);
        //THEN the repository is called only once & it gets the same parameter as the viewModel
        Mockito.verify(repository, times(1)).createTask(TASK_CREATED);
        Mockito.verify(repository).createTask(eq(TASK_CREATED));
    }
}
