package main.java.ru.yandex.practicum.tasks;

import java.time.LocalDateTime;
import java.util.ArrayList;

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
    public String toString() {
        String result = "Epic{" +
                "name='" + getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result + ", id=" + super.getId() +
                ", status='" + getStatus() + '\'' +
                ", idSubtask=" + idSubtask + '\'' +
                ", duration=" + getDuration() +
                ", startTime=" + getStartTime() +
                '}';
    }
}