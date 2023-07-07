package com.example.todo_sample.Model;

public class ToDoModel extends TaskId {

    private String task,due,url,description;
    private int status;

    public String getTask() {
        return task;
    }

    public String getImageUrl() {
        return url;
    }

    public String getDescription(){
        return description;
    }

    public String getDue() {
        return due;
    }

    public int getStatus() {
        return status;
    }
}
