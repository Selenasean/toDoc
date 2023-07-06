package com.cleanup.todoc;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cleanup.todoc.Utils.LiveDataTestUtils;
import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;
import com.cleanup.todoc.data.repository.Repository;
import com.cleanup.todoc.ui.MainViewModel;
import com.cleanup.todoc.ui.TaskViewState;
import com.google.common.truth.Truth;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;

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

    // SETTINGS FOR DATA
    private List<Project> projects;

    @Before
    public void setup() {
        projects = Arrays.asList(
                new Project(1L, "Projet Tartampion", 0xFFEADAD1),
                new Project(2L, "Projet Lucidia", 0xFFB4CDBA),
                new Project(3L, "Projet Circus", 0xFFA3CED2)
        );
        LiveData<List<Project>> projectsLiveData = new MutableLiveData<>(projects);
        Mockito.when(repository.getProjects()).thenReturn(projectsLiveData);
        viewModel = new MainViewModel(repository);
    }

    //TODO : test ViewModel methods
    //SETTING FOR TEST
    private Task TASK = new Task(2L, "bbb", 2);

    @Test
    public void getProjectById_withSuccess() throws InterruptedException {
//        // WHEN
//        viewModel.createTask(TASK);
//        List<Project> projectsList = LiveDataTestUtils.getOrAwaitValue(viewModel.getProjects());
//        Project project =  viewModel.getProjectById(projectsList, TASK.getProjectId());
//        // THEN
//        Truth.assertThat(project.getId()).isEqualTo(2L);

    }

    @Test
    public void sortByAlphabetical_withSuccess(){


    }



    @Test
    public void sortByAlphabeticalInverted_withSuccess(){

    }

    @Test
    public void sortByOldestFirst_withSuccess(){}

    @Test
    public void sortByRecentFirst_withSuccess(){}

}
