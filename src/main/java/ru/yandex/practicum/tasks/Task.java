package main.java.ru.yandex.practicum.tasks;

import main.java.ru.yandex.practicum.managers.TaskStatus;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = TaskStatus.NEW;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String result = "Task{" +
                "name='" + name + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + description.length();
        } else {
            result = result + ", description.length=null";
        }
        return result + ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
