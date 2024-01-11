package main.java.ru.yandex.practicum.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private TaskStatus status;
    private int duration;
    private LocalDateTime startTime;
    private TaskType type;

    public Task(String name, String description, int duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.startTime = startTime;
        this.status = TaskStatus.NEW;
        this.type = TaskType.TASK;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null && duration > 0) {
            return startTime.plusMinutes(duration);
        } else {
            return null;
        }
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && duration == task.duration && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status && Objects.equals(startTime, task.startTime) && type == task.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, duration, startTime, type);
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
        result = result + ", id=" + id +
                ", status='" + status + '\'' +
                ", duration=" + duration;
        if (startTime != null) {
            result = result + ", startTime=" + startTime.format(DateTimeFormatter.ofPattern("HH.mm dd.MM.yyyy"));
        } else {
            result = result + ", startTime=null";
        }
        return result +
                '}';
    }
}