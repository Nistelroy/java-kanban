package main.java.ru.yandex.practicum.tasks;

import java.util.ArrayList;
import java.util.Objects;

public class Epic extends Task {
    private ArrayList<Integer> idSubtask = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        setStatus(TaskStatus.NEW);
    }

    public Epic(String name, String description, int id, TaskStatus status, ArrayList<Integer> idSubtask) {
        super(name, description, id, status);
        this.idSubtask = idSubtask;
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

    @Override
    public String toString() {
        String result = "Epic{" +
                "name='" + super.getName() + '\'';
        if (getDescription() != null) {
            result = result + ", description.length=" + super.getDescription().length();
        } else {
            result = result + ", description.length=null";
        }
        return result + ", id=" + super.getId() +
                ", status='" + getStatus() + '\'' +
                ", idSubtask=" + idSubtask + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(idSubtask, epic.idSubtask);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), idSubtask);
    }
}


