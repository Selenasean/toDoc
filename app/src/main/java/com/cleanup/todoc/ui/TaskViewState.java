package com.cleanup.todoc.ui;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

/**
 * ViewState for Task, which is the model the data have to fit into
 */
public class TaskViewState {

    private long id;

    @NonNull
    private final String nameTask;

    @ColorInt
    private final int colorProject;

    public TaskViewState(long id, @NonNull String name, int colorProject) {
        this.id = id;
        this.nameTask = name;
        this.colorProject = colorProject;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getNameTask() {
        return nameTask;
    }

    @ColorInt
    public int getColorProject() {
        return colorProject;
    }
}
