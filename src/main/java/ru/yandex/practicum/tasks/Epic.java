package main.java.ru.yandex.practicum.tasks;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private final ArrayList<Integer> idSubtask = new ArrayList<>();
    private LocalDateTime endTime;

    public Epic(String name, String description) {
        super(name, description, 0, null);
        setStatus(TaskStatus.NEW);
        setType(TaskType.EPIC);
    }

    public ArrayList<Integer> getIdSubtask() {
        return idSubtask;
    }

    public void setIdSubtask(int idNewSubtask) {
        idSubtask.add(idNewSubtask);
    }

    public void removeEpicSubtasksById(Integer remId) {
        idSubtask.remove(remId);
    }

    public void removeAllSubtasksInEpic() {
        idSubtask.clear();
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId()
                && getDuration() == epic.getDuration()
                && Objects.equals(getName(), epic.getName())
                && Objects.equals(getDescription(), epic.getDescription())
                && getStatus() == epic.getStatus()
                && Objects.equals(getStartTime(), epic.getStartTime())
                && getType() == epic.getType()
                && Objects.equals(idSubtask, epic.idSubtask)
                && Objects.equals(endTime, epic.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtask, endTime);
    }

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        result = result + ", id=" + getId() +
                ", status='" + getStatus() + '\'' +
                ", idSubtask=" + idSubtask + '\'' +
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