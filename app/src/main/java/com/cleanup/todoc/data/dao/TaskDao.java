package com.cleanup.todoc.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cleanup.todoc.data.model.Task;

import java.util.List;


@Dao
public interface TaskDao {

    @Query("SELECT * FROM Task")
    LiveData<List<Task>> getTasks();

    @Insert
    void createTask(Task task);

    @Query("DELETE FROM Task WHERE id = :taskId")
    void deleteTask(long taskId);

}
