package com.cleanup.todoc.data.model;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Comparator;

/**
 * <p>Model for the tasks of the application.</p>
 *
 * @author Gaëtan HERFRAY
 */
public class Task {
    /**
     * The unique identifier of the task
     */
    private long id;

    /**
     * The unique identifier of the project associated to the task
     */
    private long projectId;

    /**
     * The name of the task
     */
    @NonNull
    public String name;

    /**
     * The timestamp when the task has been created
     */
    private long creationTimestamp;


    /**
     * Instantiates a new Task.
     *
     * @param id                the unique identifier of the task to set
     * @param projectId         the unique identifier of the project associated to the task to set
     * @param name              the name of the task to set
     * @param creationTimestamp the timestamp when the task has been created to set
     */
    public Task(long id, long projectId, @NonNull String name, long creationTimestamp) {
        this.id = id;
        this.projectId = projectId;
        this.name = name;
        this.creationTimestamp = creationTimestamp;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }


//    /**
//     * Comparator to sort task from A to Z
//     */
//    public static class TaskAZComparator implements Comparator<Task> {
//        @Override
//        public int compare(Task left, Task right) {
//            return left.name.compareTo(right.name);
//        }
//    }
//
//    /**
//     * Comparator to sort task from Z to A
//     */
//    public static class TaskZAComparator implements Comparator<Task> {
//        @Override
//        public int compare(Task left, Task right) {
//            return right.name.compareTo(left.name);
//        }
//    }
//
//    /**
//     * Comparator to sort task from last created to first created
//     */
//    public static class TaskRecentComparator implements Comparator<Task> {
//        @Override
//        public int compare(Task left, Task right) {
//            return (int) (right.creationTimestamp - left.creationTimestamp);
//        }
//    }
//
//    /**
//     * Comparator to sort task from first created to last created
//     */
//    public static class TaskOldComparator implements Comparator<Task> {
//        @Override
//        public int compare(Task left, Task right) {
//            return (int) (left.creationTimestamp - right.creationTimestamp);
//        }
//    }
}
