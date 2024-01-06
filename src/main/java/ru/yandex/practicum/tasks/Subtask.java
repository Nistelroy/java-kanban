package main.java.ru.yandex.practicum.tasks;

import java.time.LocalDateTime;

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
    public String toString() {
        String result = "Subtask{" +
                "name='" + getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result + ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", idEpic=" + idEpic + '\'' +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}