package main.java.ru.yandex.practicum.tasks;

import main.java.ru.yandex.practicum.manager.TaskManager;

import java.util.Objects;

import static main.java.ru.yandex.practicum.manager.ConstantsStatus.*;

public class Task implements HasId {
    private String name;
    private String description;
    private int id;
    private String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.status = NEW;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
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
