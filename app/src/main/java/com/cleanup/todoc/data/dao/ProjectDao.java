package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cleanup.todoc.data.model.Project;
import com.cleanup.todoc.data.model.Task;

import java.util.List;

@Dao
public interface ProjectDao {

    @Insert
    void createProject(Project project);

    @Query("SELECT * FROM Project")
    LiveData<List<Project>> getProjects();
}
