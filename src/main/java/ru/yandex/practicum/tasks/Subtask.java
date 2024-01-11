package main.java.ru.yandex.practicum.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Subtask extends Task {
    private final int idEpic;

    public Subtask(String name, String description, int duration, LocalDateTime startTime, int idEpic) {
        super(name, description, duration, startTime);
        this.idEpic = idEpic;
        setType(TaskType.SUBTASK);
    }

    public int getIdEpic() {
        return idEpic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getId() == subtask.getId()
                && getDuration() == subtask.getDuration()
                && Objects.equals(getName(), subtask.getName())
                && Objects.equals(getDescription(), subtask.getDescription())
                && getStatus() == subtask.getStatus()
                && Objects.equals(getStartTime(), subtask.getStartTime())
                && getType() == subtask.getType()
                && getIdEpic() == subtask.getIdEpic();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idEpic);
    }

    @Override
    public String toString() {
        String result = "Subtask{" +
                "name='" + getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        result = result + ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", idEpic=" + idEpic + '\'' +
                ", duration=" + getDuration();
        if (getStartTime() != null) {
            result = result + ", startTime=" + getStartTime().format(DateTimeFormatter.ofPattern("HH.mm dd.MM.yyyy"));
        } else {
            result = result + ", startTime=null";
        }
        return result +
                '}';
    }
}